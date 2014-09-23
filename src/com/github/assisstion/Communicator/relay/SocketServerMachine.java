package com.github.assisstion.Communicator.relay;

import java.net.ServerSocket;
import java.util.List;

import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.L.SocketListenerHandler;

public interface SocketServerMachine<T extends SocketHandler<?>>
extends SocketMachine, SocketListenerHandler<T>{
	ServerSocket getServerSocket();
	List<T> getClientList();
	SocketHandlerGenerator<T> getHandlerGenerator();
}
