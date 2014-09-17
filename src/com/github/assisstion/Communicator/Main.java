package com.github.assisstion.Communicator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.ASocketServer;
import com.github.assisstion.Communicator.relay.CSocketHelper;
import com.github.assisstion.Communicator.relay.message.MessageProcessor;

public class Main{
	public static void main(String[] args){
		int port = 59026;
		MessageProcessor process = new MessageProcessor();

		try(ASocketServer<ASocketHandler> server =
				CSocketHelper.getServer(port, process);
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in))){
			//ASocketClient<ASocketHandler> client =
			//		CSocketHelper.getClient("172.25.50.172", port, process);){
			System.out.println("Started...");
			server.open();
			System.out.println("Server opened!");
			//client.open();
			System.out.println("Client opened!");
			//process.output("Hello, world!");
			//process.output("Hello, world!");
			//process.output("Hello, world!");
			boolean on = true;
			while(on){
				String s = in.readLine();
				if(s.equalsIgnoreCase("QUIT")){
					break;
				}
				process.output(s);
			}
			Thread.sleep(1000);
			System.out.println("Done!");
		}
		catch(IOException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
