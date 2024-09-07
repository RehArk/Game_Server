package net.vincent_clerc;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.vincent_clerc.entities.Entity;
import net.vincent_clerc.manager.GameManager;
import net.vincent_clerc.network.NetworkManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameServer {

    private NetworkManager networkManager;
    private GameManager gameManager;

    public GameServer(Integer port) {

        this.gameManager = new GameManager();
        this.networkManager = new NetworkManager(port,  () -> {

            Entity player = new Entity(UUID.randomUUID());
            this.gameManager.addEntity(player);

            Map<String, Object> data = new HashMap<>();

            data.put("status", "connected");
            data.put("id", player.getId().toString());

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);

        });

    }

    public void start() {
        this.gameManager.initialize();
        this.networkManager.initialize();
    }

}
