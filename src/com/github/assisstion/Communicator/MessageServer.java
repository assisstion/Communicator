package com.github.assisstion.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.assisstion.Communicator.message.MessageProcessor;
import com.markusfeng.SocketRelay.A.SocketHandler;
import com.markusfeng.SocketRelay.A.SocketServer;
import com.markusfeng.SocketRelay.C.SocketHelper;

public class MessageServer{
	public static void start(int port, MessageProcessor process) throws IOException{
		try(SocketServer<SocketHandler<String>> server =
				SocketHelper.getStringServer(port, process);
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
			System.out.println("Started...");
			server.open();
			System.out.println("Server opened!");
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