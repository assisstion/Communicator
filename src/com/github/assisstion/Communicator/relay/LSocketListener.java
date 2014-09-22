package com.github.assisstion.Communicator.relay;

import java.util.function.Consumer;

@FunctionalInterface
public interface LSocketListener<T extends ASocketHandler> extends Consumer<T>{
	//uses void accept(T handler) from superclass
}