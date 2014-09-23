package com.github.assisstion.Communicator.relay.C;

import java.io.IOException;

import com.github.assisstion.Communicator.relay.A.SocketClient;
import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketServer;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.Stream.SocketStreamHandlerGeneratorImpl;
import com.github.assisstion.Communicator.relay.Stream.SocketStreamHandlerImpl;
import com.github.assisstion.Communicator.relay.String.BSocketStringHandlerGeneratorImpl;
import com.github.assisstion.Communicator.relay.String.BSocketStringHandlerImpl;

public class SocketHelper{
	public static <T extends SocketProcessor<String>> SocketServer<SocketHandler<String>>
	getStringServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		return new SocketServer<SocketHandler<String>>(port,
				new BSocketStringHandlerGeneratorImpl(gen));
	}

	public static <T extends SocketProcessor<String>> SocketClient<SocketHandler<String>>
	getStringClient(String host, int port, SocketProcessor<String> gen)
			throws IOException{
		BSocketStringHandlerImpl handler = new BSocketStringHandlerImpl(gen);
		SocketClient<SocketHandler<String>> client = new SocketClient<SocketHandler<String>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}

	public static <T extends SocketProcessor<byte[]>> SocketServer<SocketHandler<byte[]>>
	getByteArrayServer(int port, SocketProcessorGenerator<T> gen)
			throws IOException{
		return new SocketServer<SocketHandler<byte[]>>(port,
				new SocketStreamHandlerGeneratorImpl(gen));
	}

	public static <T extends SocketProcessor<byte[]>> SocketClient<SocketHandler<byte[]>>
	getByteArrayClient(String host, int port, SocketProcessor<byte[]> gen)
			throws IOException{
		SocketStreamHandlerImpl handler = new SocketStreamHandlerImpl(gen);
		SocketClient<SocketHandler<byte[]>> client = new SocketClient<SocketHandler<byte[]>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}
}
