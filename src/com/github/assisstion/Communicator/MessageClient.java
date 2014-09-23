package com.github.assisstion.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.assisstion.Communicator.relay.ASocketClient;
import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.CSocketStringHelper;
import com.github.assisstion.Communicator.relay.message.MessageProcessor;

public class MessageClient{
	public static void start(String host, int port, MessageProcessor process) throws IOException{
		try(
				ASocketClient<ASocketHandler<String>> client =
				CSocketStringHelper.getClient(host, port, process);
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
			System.out.println("Started...");
			client.open();
			System.out.println("Client opened!");
			boolean on = true;
			while(on){
				String s = in.readLine();
				if(s.equalsIgnoreCase("QUIT")){
					break;
				}
				process.output(s, false);
			}
			System.out.println("Done!");
		}
	}
}