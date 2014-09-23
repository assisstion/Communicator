package com.github.assisstion.Communicator.relay.A;

import java.net.Socket;
import java.util.function.Function;

@FunctionalInterface
public interface SocketHandlerGenerator<T extends SocketHandler<?>> extends Function<Socket, T>{
	//uses T apply(Socket socket) from superclass
}
