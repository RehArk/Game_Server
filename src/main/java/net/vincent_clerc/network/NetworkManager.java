package net.vincent_clerc.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

public class NetworkManager {

    private final Integer port;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private ConnectionHandler connectionHandler;

    private Callable playerConnectionCallback;


    public NetworkManager(
        Integer port,
        Callable playerConnectionCallback
    ) {
        this.port = port;
        this.playerConnectionCallback = playerConnectionCallback;
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
        this.connectionHandler = new ConnectionHandler(this, this.playerConnectionCallback);

        System.out.println("Game server started on port " + port);

        while (true) {
            this.connectionHandler.handleConnection();
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

    public void sendMessage(SocketChannel clientSocket, String message) throws IOException {

        byte[] byteOutput = message.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(byteOutput.length);

        buffer.put(message.getBytes());
        buffer.flip();
        clientSocket.write(buffer);

    }

}
