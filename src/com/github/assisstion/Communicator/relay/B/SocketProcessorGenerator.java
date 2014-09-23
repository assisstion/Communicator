package com.github.assisstion.Communicator.relay.B;

import java.util.function.Supplier;

@FunctionalInterface
public interface SocketProcessorGenerator<T extends SocketProcessor<?>> extends Supplier<T>{
	//uses T get() from superclass
}
