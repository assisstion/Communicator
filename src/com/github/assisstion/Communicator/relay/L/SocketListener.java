package com.github.assisstion.Communicator.relay.L;

import java.util.function.Consumer;

import com.github.assisstion.Communicator.relay.A.SocketHandler;

@FunctionalInterface
public interface SocketListener<T extends SocketHandler<?>> extends Consumer<T>{
	//uses void accept(T handler) from superclass
}