package com.github.assisstion.Communicator.relay;

import java.io.IOException;

public class CSocketHelper{
	public static <T extends BSocketProcessor> ASocketServer<ASocketHandler>
	getServer(int port, BSocketProcessorGenerator<T> gen)
			throws IOException{
		return new ASocketServer<ASocketHandler>(port,
				new BSocketHandlerGeneratorImpl(gen));
	}

	public static <T extends BSocketProcessor> ASocketClient<ASocketHandler>
	getClient(String host, int port, BSocketProcessor gen)
			throws IOException{
		BSocketHandlerImpl handler = new BSocketHandlerImpl(gen);
		ASocketClient<ASocketHandler> client = new ASocketClient<ASocketHandler>(host, port,
				handler);
		handler.openSocket(client.getClientSocket());
		return client;
	}
}
