package com.github.assisstion.Communicator.message;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.github.assisstion.Communicator.relay.A.ASocketClient;
import com.github.assisstion.Communicator.relay.A.ASocketHandler;
import com.github.assisstion.Communicator.relay.A.ASocketServer;
import com.github.assisstion.Communicator.relay.C.CSocketHelper;

public final class AudioMessageProcessorHelper{

	private AudioMessageProcessorHelper(){
		//Do nothing
	}

	public static void main(String[] args){
		try{
			new Thread(() ->{
				try{
					startServer(new AudioMessageProcessor(), 50010);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}).start();

			new Thread(() ->{
				try{
					startClient(new AudioMessageProcessor(), "localhost", 50010);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}).start();

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
		AudioFormat format = AudioMessageProcessor.getFormat();
		DataLine.Info info = new DataLine.Info(
				TargetDataLine.class, format);
		TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		p.write(new AudioInputStream(line), false);
	}
}
