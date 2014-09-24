package com.github.assisstion.Communicator.relay.C;

import java.io.IOException;

import com.github.assisstion.Communicator.relay.A.SocketClient;
import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketServer;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.Stream.SocketStreamHandler;
import com.github.assisstion.Communicator.relay.Stream.SocketStreamHandlerGenerator;
import com.github.assisstion.Communicator.relay.String.SocketStringHandler;
import com.github.assisstion.Communicator.relay.String.SocketStringHandlerGenerator;

/**
 * A helper class used to easily generate SocketServers and SocketClients
 *
 * @author Markus Feng
 */
public final class SocketHelper{

	private SocketHelper(){
		//Do nothing
	}

	/**
	 * Creates a String SocketServer
	 * @param port the port of the server
	 * @param gen the SocketProcessorGenerator of the handler of the server
	 * @return the SocketServer generated
	 * @throws IOException
	 */
	public static <T extends SocketProcessor<String>> SocketServer<SocketHandler<String>>
	getStringServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		return new SocketServer<SocketHandler<String>>(port,
				new SocketStringHandlerGenerator(gen));
	}

	/**
	 * Creates a String SocketClient
	 * @param host the host of the client
	 * @param port the port of the client
	 * @param process the SocketProcess of the handler of the client
	 * @return the SocketClient generated
	 * @throws IOException
	 */
	public static <T extends SocketProcessor<String>> SocketClient<SocketHandler<String>>
	getStringClient(String host, int port, SocketProcessor<String> process)
			throws IOException{
		SocketStringHandler handler = new SocketStringHandler(process);
		SocketClient<SocketHandler<String>> client = new SocketClient<SocketHandler<String>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}

	/**
	 * Creates a byte[] SocketServer
	 * @param port the port of the server
	 * @param gen the SocketProcessorGenerator of the handler of the server
	 * @return the SocketServer generated
	 * @throws IOException
	 */
	public static <T extends SocketProcessor<byte[]>> SocketServer<SocketHandler<byte[]>>
	getByteArrayServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		return new SocketServer<SocketHandler<byte[]>>(port,
				new SocketStreamHandlerGenerator(gen));
	}

	/**
	 * Creates a byte[] SocketClient
	 * @param host the host of the client
	 * @param port the port of the client
	 * @param process the SocketProcess of the handler of the client
	 * @return the SocketClient generated
	 * @throws IOException
	 */
	public static <T extends SocketProcessor<byte[]>> SocketClient<SocketHandler<byte[]>>
	getByteArrayClient(String host, int port, SocketProcessor<byte[]> gen)
			throws IOException{
		SocketStreamHandler handler = new SocketStreamHandler(gen);
		SocketClient<SocketHandler<byte[]>> client = new SocketClient<SocketHandler<byte[]>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}
}
