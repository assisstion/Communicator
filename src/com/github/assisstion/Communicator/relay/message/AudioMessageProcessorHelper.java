package com.github.assisstion.Communicator.relay.message;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.github.assisstion.Communicator.relay.ASocketClient;
import com.github.assisstion.Communicator.relay.ASocketHandler;
import com.github.assisstion.Communicator.relay.ASocketServer;
import com.github.assisstion.Communicator.relay.CSocketHelper;

public final class AudioMessageProcessorHelper{

	private AudioMessageProcessorHelper(){
		//Do nothing
	}

	public static void main(String[] args){
		try{
			startServer(new AudioMessageProcessor(), 50010);
		}
		catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void startServer(AudioMessageProcessor p, int port) throws IOException, LineUnavailableException{
		ASocketServer<ASocketHandler<byte[]>> server = CSocketHelper.getByteArrayServer(port, p);
		server.open();
		startWrite(p);
	}

	public static void startClient(AudioMessageProcessor p, String host, int port) throws IOException, LineUnavailableException{
		ASocketClient<ASocketHandler<byte[]>> client = CSocketHelper.getByteArrayClient(host, port, p);
		client.open();
		startWrite(p);
	}

	public static void startWrite(AudioMessageProcessor p) throws IOException, LineUnavailableException{
		p.setEnableWriting(true);
		DataLine.Info info = new DataLine.Info(
				TargetDataLine.class, AudioMessageProcessor.getFormat());
		TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		p.write(new AudioInputStream(line), false);
	}
}
