package com.github.assisstion.Communicator;

import java.io.IOException;

import com.github.assisstion.Communicator.message.MessageProcessor;

public class Main{
	public static void main(String[] args){
		int port = 59026;
		MessageProcessor process = new MessageProcessor();
		String host = "172.25.50.172";
		try{
			MessageClient.start(host, port, process);
		}
		catch(IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
