package com.github.assisstion.Communicator.message;

import com.github.assisstion.Communicator.gui.CustomLevel;


public class MessageCommandProcessorImpl implements
MessageCommandProcessor{

	protected MessageProcessor mp;

	public MessageCommandProcessorImpl(MessageProcessor mp){
		setMessageProcessor(mp);
	}

	public MessageCommandProcessorImpl(){

	}

	@Override
	public void setMessageProcessor(MessageProcessor mp){
		this.mp = mp;
	}

	@Override
	public String processOut(String out){
		if(out.contains("[") || out.contains("]")){
			out = out.replace('[', '(');
			out = out.replace(']', ')');
		}
		if(out.startsWith("/")){
			String cmd = out.substring(1);
			return "[" + cmd + "]";
		}
		return out;
	}

	@Override
	public String processIn(String in){
		return in;
	}

	@Override
	public boolean isCommand(String command){
		System.out.println("Checking... " + command);
		if(command.startsWith("[") && command.endsWith("]")){
			return true;
		}
		return false;
	}

	@Override
	public String runCommand(String command, boolean isGoingOut){
		if(!isCommand(command)){
			throw new IllegalArgumentException("Illegal command");
		}
		else{
			System.out.println("Executing Command: " + command);
			String commandName = getCommandName(command);
			System.out.println("Command Name: " + commandName);
			if(isGoingOut){
				if(commandName.equals("call")){
					AudioMessageProcessor amp = mp.getAudioProcess();
					if(amp != null){
						amp.setEnableWriting(true);
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Outgoing call started!");

					}
					else{
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Cannot initiate outgoing call!");
					}
					return "[callin]";
				}
				else if(commandName.equals("endcall")){
					AudioMessageProcessor amp = mp.getAudioProcess();
					if(amp != null){
						amp.setEnableWriting(false);
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Call ended by command!");

					}
					else{
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Cannot end call by command!");
					}
					return "[endcallin]";
				}
				else{
					mp.getLogger().log(CustomLevel.NOMESSAGE, "Invalid outgoing command!");
				}
			}
			else{
				if(commandName.equals("callin")){
					AudioMessageProcessor amp = mp.getAudioProcess();
					if(amp != null){
						amp.setEnableWriting(true);
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Incoming call started!");
					}
					else{
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Cannot initiate incoming call!");
					}
					return null;
				}
				else if(commandName.equals("endcallin")){
					AudioMessageProcessor amp = mp.getAudioProcess();
					if(amp != null){
						amp.setEnableWriting(false);
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Call ended from other end!");
					}
					else{
						mp.getLogger().log(CustomLevel.NOMESSAGE, "Cannot end call from other end!");
					}
					return null;
				}
				else{
					mp.getLogger().log(CustomLevel.NOMESSAGE, "Invalid incoming command!");
				}
			}
			return null;
		}
	}

	private static String getCommandName(String command){
		return command.substring(1, command.length() - 1).split(" ")[0];
	}

}
