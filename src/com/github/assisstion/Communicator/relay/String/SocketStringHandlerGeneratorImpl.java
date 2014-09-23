package com.github.assisstion.Communicator.relay.String;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;

public class SocketStringHandlerGeneratorImpl implements
SocketHandlerGenerator<SocketHandler<String>>{

	protected SocketProcessorGenerator<? extends SocketProcessor<String>> generator;

	public SocketStringHandlerGeneratorImpl(SocketProcessorGenerator<? extends SocketProcessor<String>> gen){
		generator = gen;
	}

	@Override
	public SocketHandler<String> apply(Socket socket){
		return new SocketStringHandlerImpl(socket, generator.get());
	}

}