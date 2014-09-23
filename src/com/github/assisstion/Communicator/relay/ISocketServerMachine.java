package com.github.assisstion.Communicator.relay;

import java.net.ServerSocket;
import java.util.List;

import com.github.assisstion.Communicator.relay.A.ASocketHandler;
import com.github.assisstion.Communicator.relay.A.ASocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.L.LSocketListenerHandler;

public interface ISocketServerMachine<T extends ASocketHandler<?>>
extends ISocketMachine, LSocketListenerHandler<T>{
	ServerSocket getServerSocket();
	List<T> getClientList();
	ASocketHandlerGenerator<T> getHandlerGenerator();
}
