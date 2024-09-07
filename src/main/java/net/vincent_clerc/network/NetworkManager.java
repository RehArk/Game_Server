package net.vincent_clerc.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class NetworkManager {

    private final Integer port;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private ConnectionHandler connectionHandler;

    public NetworkManager(Integer port) {
        this.port = port;    
    }

    public ServerSocketChannel getServerSocketChannel() {
        return this.serverSocketChannel;
    }

    public Selector getSelector() {
        return this.selector;
    }

    public void initialize() {

        System.out.println("Game server will start on port " + port);

        this.initializeConnection();
        this.createConnectionHandler();

        System.out.println("Game server started on port " + port);

        while (true) {
            this.handleConnection();
        }

    }

    private void initializeConnection() {

        try {
            setupServerSocket();
            setupSelector();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

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

    private void createConnectionHandler() {

        this.connectionHandler = new ConnectionHandler(this, () -> {
            System.out.println("Connexion handled");
            return null;
        });

    }

    private void handleConnection() {
        this.connectionHandler.handleConnection();
    }

}
