package com.github.assisstion.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.ASocketServer;
import com.github.assisstion.Communicator.relay.CSocketHelper;
import com.github.assisstion.Communicator.relay.message.MessageProcessor;

public class MessageServer{
	public static void start(int port, MessageProcessor process) throws IOException{
		try(ASocketServer<ASocketHandler<String>> server =
				CSocketHelper.getStringServer(port, process);
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
			System.out.println("Started...");
			server.open();
			System.out.println("Server opened!");
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