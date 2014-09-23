package com.github.assisstion.Communicator.relay.A;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public interface ASocketHandler<T> extends Runnable, Closeable{

	void push(T out) throws IOException;
	Socket getSocket();
}
