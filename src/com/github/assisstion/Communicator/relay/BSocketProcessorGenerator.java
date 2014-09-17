package com.github.assisstion.Communicator.relay;

public interface BSocketProcessorGenerator<T extends BSocketProcessor>{
	T generate();
}
