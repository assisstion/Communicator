package com.github.assisstion.Communicator.relay.message;

import com.github.assisstion.Communicator.relay.BSocketProcessorGenerator;

public class MessageProcessorGenerator implements BSocketProcessorGenerator<MessageProcessor>{

	@Override
	public MessageProcessor get(){
		return new MessageProcessor();
	}

}
