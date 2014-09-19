package com.github.assisstion.Communicator.relay.message;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.BSocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.CSocketProcessorAbstract;


public class MessageProcessor extends CSocketProcessorAbstract implements BSocketProcessorGenerator<MessageProcessor>{

	public Logger logger = null;

	public MessageProcessor(){

	}

	@Override
	public void attachHandler(ASocketHandler handler){
		String info = "Attached handler: " + handler.getSocket();
		if(logger == null){
			System.out.println(info);
		}
		else{
			if(logger.isLoggable(Level.INFO)){
				logger.info(info);
			}
		}
	}

	@Override
	public void removeHandler(ASocketHandler handler){
		String info = "Removed handler: " + handler.getSocket();
		if(logger == null){
			System.out.println(info);
		}
		else{
			if(logger.isLoggable(Level.INFO)){
				logger.info(info);
			}
		}
	}

	public MessageProcessor(Logger log){
		logger = log;
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
		if(logger == null){
			System.out.println(in);
		}
		else{
			if(logger.isLoggable(Level.INFO)){
				logger.info(in);
			}
		}
	}

	@Override
	public MessageProcessor generate(){
		return this;
	}
}
