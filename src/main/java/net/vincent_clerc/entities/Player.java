package net.vincent_clerc.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.nio.channels.SocketChannel;
import java.util.UUID;

public class Player extends Entity {

    private final SocketChannel socket;

    public Player(SocketChannel socket, UUID id) {
        super(id, 0,0,0);
        this.socket = socket;
    }

    @JsonIgnore
    public SocketChannel getSocket() {
        return socket;
    }

}
