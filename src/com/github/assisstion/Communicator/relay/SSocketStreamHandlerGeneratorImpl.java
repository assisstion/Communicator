package com.github.assisstion.Communicator.relay;

import java.net.Socket;

public class SSocketStreamHandlerGeneratorImpl implements
ASocketHandlerGenerator<ASocketHandler<byte[]>>{

	protected BSocketProcessorGenerator<? extends BSocketProcessor<byte[]>> generator;

	public SSocketStreamHandlerGeneratorImpl(BSocketProcessorGenerator<? extends BSocketProcessor<byte[]>> gen){
		generator = gen;
	}

	@Override
	public ASocketHandler<byte[]> apply(Socket socket){
		return new SSocketStreamHandlerImpl(socket, generator.get());
	}

}