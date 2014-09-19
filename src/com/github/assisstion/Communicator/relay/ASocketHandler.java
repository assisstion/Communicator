package com.github.assisstion.Communicator.relay;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public interface ASocketHandler extends Runnable, Closeable{

	void push(String out) throws IOException;
	Socket getSocket();
}
