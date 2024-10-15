package net.vincent_clerc;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.vincent_clerc.entities.Player;
import net.vincent_clerc.manager.GameManager;
import net.vincent_clerc.network.MessageProcessor;
import net.vincent_clerc.network.NetworkManager;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServer {

    private GameManager gameManager;
    private NetworkManager networkManager;

    public GameServer(Integer port) {
        this.gameManager = new GameManager(this::broadcastGameState);
        this.networkManager = new NetworkManager(
            port,
            this::playerConnectionCallback,
                this::playerDataReadCallback,
            this::playerDisconnectionCallback
        );
    }

    public void playerConnectionCallback(SocketChannel socketChannel) {

        Player player = new Player(socketChannel, UUID.randomUUID());
        this.gameManager.getEntityManager().addPlayer(player);

        Map<String, Object> data = new HashMap<>();

        data.put("status", "connected");
        data.put("id", player.getId());

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String output = objectMapper.writeValueAsString(data);
            this.networkManager.sendMessage(socketChannel, output);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Player " + player.getId() + " connected");

    }

    public void playerDataReadCallback(SocketChannel clientChannel, String data) {
        Player player = this.gameManager.getEntityManager().getPlayerByChannel(clientChannel);
        MessageProcessor messageProcessor = new MessageProcessor();
        messageProcessor.processMessage(player, data);
    }

    public void playerDisconnectionCallback(SocketChannel clientChannel) {
        Player player = this.gameManager.getEntityManager().getPlayerByChannel(clientChannel);
        this.gameManager.getEntityManager().removePlayer(player);
    }

    public void start() {
        this.gameManager.initialize();
        this.networkManager.initialize();
        System.out.println("Game server running");
    }

    public void broadcastGameState() {

        Map<String, Object> gameState = gameManager.getGameState();
        Iterable<Player> players = this.gameManager.getAllPlayers();

        this.networkManager.broadcastGameState(gameState, players);

    }

}
