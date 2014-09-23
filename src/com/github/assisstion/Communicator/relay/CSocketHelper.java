package com.github.assisstion.Communicator.relay;

import java.io.IOException;

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
