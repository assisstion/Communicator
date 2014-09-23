package com.github.assisstion.Communicator.relay;

import java.net.Socket;

public class BSocketHandlerGeneratorImpl implements
ASocketHandlerGenerator<ASocketHandler<String>>{

	protected BSocketProcessorGenerator<? extends BSocketProcessor<String>> generator;

	public BSocketHandlerGeneratorImpl(BSocketProcessorGenerator<? extends BSocketProcessor<String>> gen){
		generator = gen;
	}

	@Override
	public ASocketHandler<String> apply(Socket socket){
		return new BSocketHandlerImpl(socket, generator.get());
	}

}