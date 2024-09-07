package net.vincent_clerc.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ConnectionHandler {

    private NetworkManager manager;
    private Callable playerConnectionCallback;

    public ConnectionHandler(
        NetworkManager manager,
        Callable playerConnectionCallback
    ) {
        this.manager = manager;
        this.playerConnectionCallback = playerConnectionCallback;
    }

    public void handleConnection() {

        try{

            this.manager.getSelector().select();
            Iterator<SelectionKey> keys = this.manager.getSelector().selectedKeys().iterator();

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

        SocketChannel clientSocket = this.manager.getServerSocketChannel().accept();
        clientSocket.configureBlocking(false);
        clientSocket.register(this.manager.getSelector(), SelectionKey.OP_READ);

        try {
            String output = (String) this.playerConnectionCallback.call();
            this.manager.sendMessage(clientSocket, output);
        } catch (Exception e) {
            System.out.println();
        }

    }

    private void readData(SelectionKey key) {

    }

}
