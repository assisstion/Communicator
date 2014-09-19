package com.github.assisstion.Communicator.relay;

import java.net.ServerSocket;
import java.util.List;

public interface ISocketServerMachine<T extends ASocketHandler>
extends ISocketMachine, LSocketListenerHandler<T>{
	ServerSocket getServerSocket();
	List<T> getClientList();
	ASocketHandlerGenerator<T> getHandlerGenerator();
}
