package com.github.assisstion.Communicator.relay;

public interface LSocketListener<T extends ASocketHandler>{
	void attach(T handler);
}