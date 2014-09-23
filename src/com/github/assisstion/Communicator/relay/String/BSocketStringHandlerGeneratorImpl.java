package com.github.assisstion.Communicator.relay.String;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.ASocketHandler;
import com.github.assisstion.Communicator.relay.A.ASocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.B.BSocketProcessor;
import com.github.assisstion.Communicator.relay.B.BSocketProcessorGenerator;

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