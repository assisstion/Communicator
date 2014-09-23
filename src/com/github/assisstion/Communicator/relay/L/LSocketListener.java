package com.github.assisstion.Communicator.relay.L;

import java.util.function.Consumer;

import com.github.assisstion.Communicator.relay.A.ASocketHandler;

@FunctionalInterface
public interface LSocketListener<T extends ASocketHandler<?>> extends Consumer<T>{
	//uses void accept(T handler) from superclass
}