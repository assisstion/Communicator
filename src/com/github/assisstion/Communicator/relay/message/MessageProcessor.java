package com.github.assisstion.Communicator.relay.message;

import java.io.IOException;

import com.github.assisstion.Communicator.relay.BSocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.CAbstractSocketProcessor;


public class MessageProcessor extends CAbstractSocketProcessor implements BSocketProcessorGenerator<MessageProcessor>{

	//Sends the message, then prints "Sent!"
	@Override
	public void output(String out) throws IOException{
		super.output(out);
		//System.out.println("Sent!");
	}

	//Sleeps for 1 second and prints the message
	@Override
	public void input(String in){
		try{
			Thread.sleep(1);
		}
		catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(in);
	}

	@Override
	public MessageProcessor generate(){
		return this;
	}

}
