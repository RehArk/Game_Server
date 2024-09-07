package net.vincent_clerc;

import net.vincent_clerc.network.NetworkManager;

public class GameServer {

    private NetworkManager networkManager;

    public GameServer(Integer port) {
        this.networkManager = new NetworkManager(port);
    }

    public void start() {
        this.networkManager.initialize();
    }

}
