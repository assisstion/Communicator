package com.github.assisstion.Communicator.relay;

import java.net.Socket;

public class BSocketStringHandlerGeneratorImpl implements
ASocketHandlerGenerator<ASocketHandler<String>>{

	protected BSocketProcessorGenerator<? extends BSocketProcessor<String>> generator;

	public BSocketStringHandlerGeneratorImpl(BSocketProcessorGenerator<? extends BSocketProcessor<String>> gen){
		generator = gen;
	}

	@Override
	public ASocketHandler<String> apply(Socket socket){
		return new BSocketStringHandlerImpl(socket, generator.get());
	}

}