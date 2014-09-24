package com.github.assisstion.Communicator.relay;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.SocketHandler;

/**
 * Represents a client SocketMachine that contains data about the client
 * side socket. Contains a SocketHandler that handles all connections with
 * other sources.
 *
 * @author Markus Feng
 *
 * @param <T> The type of SocketHandler used in this machine.
 */
public interface SocketClientMachine<T extends SocketHandler<?>> extends SocketMachine{

	/**
	 * Returns the Socket that is owned by this client
	 * @return the Socket that is owned by this client
	 */
	Socket getClientSocket();

	/**
	 * Returns the handler that this client uses for its connections
	 * @return the handler that this client uses for its connections
	 */
	T getHandler();
}
