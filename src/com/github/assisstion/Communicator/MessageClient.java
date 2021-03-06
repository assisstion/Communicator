package com.github.assisstion.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.assisstion.Communicator.message.MessageProcessor;
import com.markusfeng.SocketRelay.A.SocketClient;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.C.SocketHelper;

public class MessageClient{
	public static void start(String host, int port, MessageProcessor process) throws IOException{
		try(
				SocketClient<SocketHandler<String>> client =
				SocketHelper.getStringClient(host, port, process);
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
			System.out.println("Started...");
			client.open();
			System.out.println("Client opened!");
			boolean on = true;
			while(on){
				String s = in.readLine();
				if(s == null || s.equalsIgnoreCase("QUIT")){
					break;
				}
				process.output(s, false);
			}
			System.out.println("Done!");
		}
	}
}