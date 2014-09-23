package com.github.assisstion.Communicator.relay.Stream;

import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.A.SocketHandlerGenerator;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;

public class SocketStreamHandlerGeneratorImpl implements
SocketHandlerGenerator<SocketHandler<byte[]>>{

	protected SocketProcessorGenerator<? extends SocketProcessor<byte[]>> generator;

	public SocketStreamHandlerGeneratorImpl(SocketProcessorGenerator<? extends SocketProcessor<byte[]>> gen){
		generator = gen;
	}

	@Override
	public SocketHandler<byte[]> apply(Socket socket){
		return new SocketStreamHandlerImpl(socket, generator.get());
	}

}