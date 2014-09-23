package com.github.assisstion.Communicator.relay.C;

import java.io.IOException;

import com.github.assisstion.Communicator.relay.A.ASocketClient;
import com.github.assisstion.Communicator.relay.A.ASocketHandler;
import com.github.assisstion.Communicator.relay.A.ASocketServer;
import com.github.assisstion.Communicator.relay.B.BSocketProcessor;
import com.github.assisstion.Communicator.relay.B.BSocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.Stream.SSocketStreamHandlerGeneratorImpl;
import com.github.assisstion.Communicator.relay.Stream.SSocketStreamHandlerImpl;
import com.github.assisstion.Communicator.relay.String.BSocketStringHandlerGeneratorImpl;
import com.github.assisstion.Communicator.relay.String.BSocketStringHandlerImpl;

public class CSocketHelper{
	public static <T extends BSocketProcessor<String>> ASocketServer<ASocketHandler<String>>
	getStringServer(int port, BSocketProcessorGenerator<T> gen)
			throws IOException{
		return new ASocketServer<ASocketHandler<String>>(port,
				new BSocketStringHandlerGeneratorImpl(gen));
	}

	public static <T extends BSocketProcessor<String>> ASocketClient<ASocketHandler<String>>
	getStringClient(String host, int port, BSocketProcessor<String> gen)
			throws IOException{
		BSocketStringHandlerImpl handler = new BSocketStringHandlerImpl(gen);
		ASocketClient<ASocketHandler<String>> client = new ASocketClient<ASocketHandler<String>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}

	public static <T extends BSocketProcessor<byte[]>> ASocketServer<ASocketHandler<byte[]>>
	getByteArrayServer(int port, BSocketProcessorGenerator<T> gen)
			throws IOException{
		return new ASocketServer<ASocketHandler<byte[]>>(port,
				new SSocketStreamHandlerGeneratorImpl(gen));
	}

	public static <T extends BSocketProcessor<byte[]>> ASocketClient<ASocketHandler<byte[]>>
	getByteArrayClient(String host, int port, BSocketProcessor<byte[]> gen)
			throws IOException{
		SSocketStreamHandlerImpl handler = new SSocketStreamHandlerImpl(gen);
		ASocketClient<ASocketHandler<byte[]>> client = new ASocketClient<ASocketHandler<byte[]>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}
}
