package com.github.assisstion.Communicator.relay;

import java.io.IOException;

public abstract class CAbstractSocketProcessor implements BSocketProcessor{

	protected ASocketHandler handler;

	@Override
	public void attachHandler(ASocketHandler handler){
		this.handler = handler;
	}

	public void output(String out) throws IOException{
		handler.push(out);
	}
}
