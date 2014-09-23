package com.github.assisstion.Communicator.relay.A;

import java.net.Socket;
import java.util.function.Function;

@FunctionalInterface
public interface ASocketHandlerGenerator<T extends ASocketHandler<?>> extends Function<Socket, T>{
	//uses T apply(Socket socket) from superclass
}
