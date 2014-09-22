package com.github.assisstion.Communicator.relay;

import java.net.Socket;

public class BSocketHandlerGeneratorImpl implements
ASocketHandlerGenerator<ASocketHandler>{

	protected BSocketProcessorGenerator<? extends BSocketProcessor> generator;

	public BSocketHandlerGeneratorImpl(BSocketProcessorGenerator<? extends BSocketProcessor> gen){
		generator = gen;
	}

	@Override
	public ASocketHandler apply(Socket socket){
		return new BSocketHandlerImpl(socket, generator.get());
	}

}