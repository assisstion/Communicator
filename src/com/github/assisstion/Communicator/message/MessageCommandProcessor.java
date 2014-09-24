package com.github.assisstion.Communicator.message;

public interface MessageCommandProcessor{

	//Processes the output before commands are executed
	String processOut(String out);

	//Processes the input after commands are executed
	String processIn(String in);

	//Should runCommand be ran on the string?
	boolean isCommand(String possibleCommand);

	//Run the command, with the boolean telling if the command is for output
	String runCommand(String command, boolean isGoingOut);

	void setMessageProcessor(MessageProcessor mp);
}
