package com.github.assisstion.Communicator.relay.Stream;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.ASocketHandler;
import com.github.assisstion.Communicator.relay.A.ASocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.B.BSocketProcessor;
import com.github.assisstion.Communicator.relay.B.BSocketProcessorGenerator;

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