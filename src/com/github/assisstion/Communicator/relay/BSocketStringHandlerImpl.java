package com.github.assisstion.Communicator.relay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BSocketStringHandlerImpl extends BSocketHandlerImplAbstract<String>{

	protected PrintWriter out;
	protected BufferedReader in;

	public BSocketStringHandlerImpl(BSocketProcessor<String> processor){
		super(processor);
	}

	public BSocketStringHandlerImpl(Socket socket, BSocketProcessor<String> processor){
		super(socket, processor);
	}

	@Override
	protected void initialize() throws IOException{

		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void readFromIn() throws IOException{
		String inputLine;

		while (!closed && (inputLine = in.readLine()) != null) {
			if(!closed){
				try{
					if(!processor.isInputBlockingEnabled()){
						new Thread(new Inputtor(inputLine)).start();
					}
					else{
						new Inputtor(inputLine).run();
					}
				}
				catch(Exception e){
					if(!closed){
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void writeToOut(String out) throws IOException{
		this.out.println(out);
	}

}
