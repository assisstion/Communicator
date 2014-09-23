package com.github.assisstion.Communicator.relay.B;

import java.util.function.Supplier;

@FunctionalInterface
public interface BSocketProcessorGenerator<T extends BSocketProcessor<?>> extends Supplier<T>{
	//uses T get() from superclass
}
