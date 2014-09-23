package com.github.assisstion.Communicator.relay;

import java.io.Closeable;

public interface SocketMachine extends Closeable, Runnable{
	void open();
}
