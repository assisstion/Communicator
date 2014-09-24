package com.github.assisstion.Communicator.message;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.LineUnavailableException;

import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.C.SocketProcessorAbstract;


public class MessageProcessor extends SocketProcessorAbstract<String> implements SocketProcessorGenerator<MessageProcessor>{

	protected Logger logger = null;
	protected MessageCommandProcessor cmd;
	protected AudioMessageProcessor audioProcess;

	public MessageProcessor(boolean enableAudio){
		if(enableAudio){
			try{
				audioProcess = new AudioMessageProcessor();
			}
			catch(LineUnavailableException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public MessageProcessor(){
		this(true);
	}

	public MessageProcessor(MessageCommandProcessor mcp){
		this();
		cmd = mcp;
	}

	public AudioMessageProcessor getAudioProcess(){
		return audioProcess;
	}

	@Override
	public void attachHandler(SocketHandler<String> handler){
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
	public void removeHandler(SocketHandler<String> handler){
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

	@Override
	public void close() throws IOException{
		AudioMessageProcessor amp = getAudioProcess();
		if(amp != null){
			amp.close();
		}
	}
}
