package net.vincent_clerc.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.vincent_clerc.entities.Player;
import net.vincent_clerc.utils.callbacks.PlayerConnectionCallback;
import net.vincent_clerc.utils.callbacks.PlayerDataReadCallback;
import net.vincent_clerc.utils.callbacks.PlayerDisconnectionCallback;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkManager {

    private final Integer port;

    private ExecutorService executorService;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private ConnectionHandler connectionHandler;

    public NetworkManager(
        Integer port,
        PlayerConnectionCallback playerConnectionCallback,
        PlayerDataReadCallback playerDataReadCallback,
        PlayerDisconnectionCallback playerDisconnectionCallback
    ) {

        this.port = port;
        this.initializeConnection();

        this.connectionHandler = new ConnectionHandler(
                this.serverSocketChannel,
                this.selector,
                playerConnectionCallback,
                playerDataReadCallback,
                playerDisconnectionCallback
        );

    }

    public ServerSocketChannel getServerSocketChannel() {
        return this.serverSocketChannel;
    }

    public Selector getSelector() {
        return this.selector;
    }

    public void initialize() {

        this.executorService = Executors.newSingleThreadExecutor(); // Create a single-threaded executor

        executorService.execute(() -> {
            while (true) {
                this.connectionHandler.handleConnection();
            }
        });

    }

    private void initializeConnection() {

        System.out.println("Game server will listen on port " + port);

        try {
            setupServerSocket();
            setupSelector();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("Game server listen on port " + port);

    }

    private void setupServerSocket() throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.bind(new InetSocketAddress(this.port));
        this.serverSocketChannel.configureBlocking(false);
    }

    private void setupSelector() throws IOException {
        this.selector = Selector.open();
        this.serverSocketChannel.register(this.selector, this.serverSocketChannel.validOps());
    }

    public void sendMessage(SocketChannel clientSocket, String message) throws IOException {

        byte[] byteOutput = message.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(byteOutput.length);

        buffer.put(message.getBytes());
        buffer.flip();
        clientSocket.write(buffer);

    }

    public void broadcastGameState(Map<String, Object> gameState, Iterable<Player> players) {

        ObjectMapper objectMapper = new ObjectMapper();

        for (Player player : players) {

            try {
                this.sendMessage(player.getSocket(), objectMapper.writeValueAsString(gameState));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
