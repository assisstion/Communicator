package com.github.assisstion.Communicator.relay.message;

import com.github.assisstion.Communicator.relay.BSocketProcessorGenerator;

public class MessageProcessorGenerator implements BSocketProcessorGenerator<MessageProcessor>{

	@Override
	public MessageProcessor generate(){
		return new MessageProcessor();
	}

}
