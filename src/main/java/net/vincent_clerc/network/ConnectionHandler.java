package net.vincent_clerc.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.Callable;

public class ConnectionHandler {

    private NetworkManager manager;
    private Callable connectionCallback;

    public ConnectionHandler(
        NetworkManager manager,
        Callable connectionCallback
    ) {
        this.manager = manager;
        this.connectionCallback = connectionCallback;
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
            this.connectionCallback.call();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("Connected".getBytes());
            buffer.flip();
            clientSocket.write(buffer);
        } catch (Exception e) {
            System.out.println();
        }

    }

    private void readData(SelectionKey key) {

    }

}
