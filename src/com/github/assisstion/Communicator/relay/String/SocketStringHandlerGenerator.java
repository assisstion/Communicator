package com.github.assisstion.Communicator.relay.String;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;

public class SocketStringHandlerGenerator implements
SocketHandlerGenerator<SocketHandler<String>>{

	protected SocketProcessorGenerator<? extends SocketProcessor<String>> generator;

	public SocketStringHandlerGenerator(SocketProcessorGenerator<? extends SocketProcessor<String>> gen){
		generator = gen;
	}

	@Override
	public SocketHandler<String> apply(Socket socket){
		return new SocketStringHandler(socket, generator.get());
	}

}