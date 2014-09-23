package com.github.assisstion.Communicator.relay;

import java.io.IOException;

public class CSocketStringHelper{
	public static <T extends BSocketProcessor<String>> ASocketServer<ASocketHandler<String>>
	getServer(int port, BSocketProcessorGenerator<T> gen)
			throws IOException{
		return new ASocketServer<ASocketHandler<String>>(port,
				new BSocketStringHandlerGeneratorImpl(gen));
	}

	public static <T extends BSocketProcessor<String>> ASocketClient<ASocketHandler<String>>
	getClient(String host, int port, BSocketProcessor<String> gen)
			throws IOException{
		BSocketStringHandlerImpl handler = new BSocketStringHandlerImpl(gen);
		ASocketClient<ASocketHandler<String>> client = new ASocketClient<ASocketHandler<String>>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}
}
