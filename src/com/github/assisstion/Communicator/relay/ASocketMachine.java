package com.github.assisstion.Communicator.relay;

import java.io.Closeable;

public interface ASocketMachine extends Closeable, Runnable{
	void open();
}
