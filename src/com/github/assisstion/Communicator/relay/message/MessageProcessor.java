package com.github.assisstion.Communicator.relay.message;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.BSocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.CSocketProcessorAbstract;


public class MessageProcessor extends CSocketProcessorAbstract<String> implements BSocketProcessorGenerator<MessageProcessor>{

	protected Logger logger = null;
	protected MessageCommandProcessor cmd;

	public MessageProcessor(){

	}

	public MessageProcessor(MessageCommandProcessor mcp){
		cmd = mcp;
	}

	@Override
	public void attachHandler(ASocketHandler<String> handler){
		super.attachHandler(handler);
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
	public void removeHandler(ASocketHandler<String> handler){
		super.removeHandler(handler);
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

	@Override
	public void output(String out, boolean block) throws IOException{
		MessageCommandProcessor mcp = getCommandProcessor();
		if(mcp != null){
			out = mcp.processOut(out);
			if(mcp.isCommand(out)){
				out = mcp.runCommand(out, true);
			}
		}
		if(out != null){
			super.output(out, block);
		}
	}

	@Override
	public void input(String in){
		MessageCommandProcessor mcp = getCommandProcessor();
		if(mcp != null){
			if(mcp.isCommand(in)){
				in = mcp.runCommand(in, false);
			}
			in = mcp.processIn(in);
		}
		if(in != null){
			if(logger != null && logger.isLoggable(Level.INFO)){
				logger.info(in);
			}
			else{
				System.out.println(in);
			}
		}
	}

	@Override
	public MessageProcessor get(){
		return this;
	}

	public void setLogger(Logger logger){
		this.logger = logger;
	}

	public Logger getLogger(){
		return logger;
	}

	public void setCommandProcessor(MessageCommandProcessor mcp){
		cmd = mcp;
	}

	public MessageCommandProcessor getCommandProcessor(){
		return cmd;
	}
}
