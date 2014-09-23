package com.github.assisstion.Communicator.relay.C;

import java.io.IOException;

import com.github.assisstion.Communicator.relay.A.SocketClient;
import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketServer;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.Stream.SocketStreamHandlerGenerator;
import com.github.assisstion.Communicator.relay.Stream.SocketStreamHandler;
import com.github.assisstion.Communicator.relay.String.SocketStringHandlerGenerator;
import com.github.assisstion.Communicator.relay.String.SocketStringHandler;

public class SocketHelper{
	public static <T extends SocketProcessor<String>> SocketServer<SocketHandler<String>>
	getStringServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		return new SocketServer<SocketHandler<String>>(port,
				new SocketStringHandlerGenerator(gen));
	}

	public static <T extends SocketProcessor<String>> SocketClient<SocketHandler<String>>
	getStringClient(String host, int port, SocketProcessor<String> gen)
			throws IOException{
		SocketStringHandler handler = new SocketStringHandler(gen);
		SocketClient<SocketHandler<String>> client = new SocketClient<SocketHandler<String>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}

	public static <T extends SocketProcessor<byte[]>> SocketServer<SocketHandler<byte[]>>
	getByteArrayServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		return new SocketServer<SocketHandler<byte[]>>(port,
				new SocketStreamHandlerGenerator(gen));
	}

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
