package net.vincent_clerc.utils.callbacks;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface PlayerDisconnectionCallback<T> {
    void call(SocketChannel result);
}