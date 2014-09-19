package com.github.assisstion.Communicator.relay;

import java.io.Closeable;

public interface ISocketMachine extends Closeable, Runnable{
	void open();
}
