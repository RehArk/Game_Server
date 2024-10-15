package net.vincent_clerc.utils.callbacks;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.nio.channels.SocketChannel;

@FunctionalInterface
public interface PlayerConnectionCallback<T> {
    void call(SocketChannel result) throws JsonProcessingException;
}