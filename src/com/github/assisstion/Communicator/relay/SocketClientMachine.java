package com.github.assisstion.Communicator.relay;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.SocketHandler;

public interface SocketClientMachine<T extends SocketHandler<?>> extends SocketMachine{
	Socket getClientSocket();
	T getHandler();
}
