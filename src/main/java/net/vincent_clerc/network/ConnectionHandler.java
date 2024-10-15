package net.vincent_clerc.network;

import net.vincent_clerc.utils.callbacks.PlayerConnectionCallback;
import net.vincent_clerc.utils.callbacks.PlayerDataReadCallback;
import net.vincent_clerc.utils.callbacks.PlayerDisconnectionCallback;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ConnectionHandler {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private PlayerConnectionCallback playerConnectionCallback;
    private PlayerDataReadCallback playerDataReadCallback;
    private PlayerDisconnectionCallback playerDisconnectionCallback;

    public ConnectionHandler(
            ServerSocketChannel serverSocketChannel,
            Selector selector,
            PlayerConnectionCallback playerConnectionCallback,
            PlayerDataReadCallback playerDataReadCallback,
            PlayerDisconnectionCallback playerDisconnectionCallback
    ) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
        this.playerConnectionCallback = playerConnectionCallback;
        this.playerDataReadCallback = playerDataReadCallback;
        this.playerDisconnectionCallback = playerDisconnectionCallback;

    }

    public void handleConnection() {

        try{

            this.selector.select();
            Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();

            while (keys.hasNext()) {

                SelectionKey key = keys.next();
                keys.remove();

                if (key.isAcceptable()) {
                    acceptConnection();
                } else if (key.isReadable()) {
                    readData(key);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void acceptConnection() throws IOException {

        SocketChannel clientSocket = this.serverSocketChannel.accept();
        clientSocket.configureBlocking(false);
        clientSocket.register(this.selector, SelectionKey.OP_READ);

        try {
            this.playerConnectionCallback.call(clientSocket);
        } catch (Exception e) {
            System.out.println();
        }

    }

    private void readData(SelectionKey key) throws IOException {

        SocketChannel clientSocket = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);

        try {

            int bytesRead = clientSocket.read(buffer);

            if (bytesRead == -1) {
                this.disconnectClient(key);
                return;
            }

            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            this.playerDataReadCallback.call(clientSocket, new String(data));

        } catch (IOException e) {
            disconnectClient(key);
        }

    }

    private void disconnectClient(SelectionKey key) {

        SocketChannel clientChannel = (SocketChannel) key.channel();

        this.playerDisconnectionCallback.call(clientChannel);

        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        key.cancel();
        System.out.println("Client déconnecté.");

    }

}
