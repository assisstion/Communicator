package com.github.assisstion.Communicator;

import java.io.IOException;

import com.github.assisstion.Communicator.relay.ASocketClient;
import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.ASocketServer;
import com.github.assisstion.Communicator.relay.CSocketHelper;
import com.github.assisstion.Communicator.relay.message.MessageProcessor;
import com.github.assisstion.Communicator.relay.message.MessageProcessorGenerator;

public class Main{
	public static void main(String[] args){
		int port = 59025;
		MessageProcessor process = new MessageProcessor();

		try(ASocketServer<ASocketHandler> server =
				CSocketHelper.getServer(port, new MessageProcessorGenerator());
				ASocketClient<ASocketHandler> client =
						CSocketHelper.getClient("localhost", port, process);){
			System.out.println("Started...");
			server.open();
			System.out.println("Server opened!");
			client.open();
			System.out.println("Client opened!");
			process.output("Hello, world!");
			process.output("Hello, world!");
			process.output("Hello, world!");
			Thread.sleep(3000);
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
