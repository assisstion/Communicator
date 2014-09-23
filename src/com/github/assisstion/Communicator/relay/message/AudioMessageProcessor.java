package com.github.assisstion.Communicator.relay.message;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import com.github.assisstion.Communicator.relay.BSocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.CSocketProcessorAbstract;


public class AudioMessageProcessor extends CSocketProcessorAbstract<byte[]> implements BSocketProcessorGenerator<AudioMessageProcessor>{

	public static final int BUFFER_SIZE = 1024;

	protected AudioFormat format;
	protected SourceDataLine line;
	protected boolean enableWriting;

	public static void main(String[] args){
		try{
			startWrite();
		}
		catch(Exception e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void startWrite() throws IOException, LineUnavailableException{
		AudioMessageProcessor amp = new AudioMessageProcessor();
		amp.setEnableWriting(true);
		DataLine.Info info = new DataLine.Info(
				TargetDataLine.class, getFormat());
		TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		amp.write(new AudioInputStream(line), false);
	}

	public AudioMessageProcessor() throws LineUnavailableException{
		format = getFormat();
		DataLine.Info info = new DataLine.Info(
				SourceDataLine.class, format);
		line = (SourceDataLine) AudioSystem.getLine(info);
	}

	public void setEnableWriting(boolean en){
		enableWriting = en;
	}

	public boolean isWritingEnabled(){
		return enableWriting;
	}

	public void write(AudioInputStream in, boolean outblocking) throws IOException{
		byte[] buf = new byte[BUFFER_SIZE];
		int count = in.read(buf);
		while(enableWriting && count >= 0){
			byte[] outArr = new byte[count];
			System.arraycopy(buf, 0, outArr, 0, count);
			output(outArr, outblocking);
			count = in.read(buf);
		}
	}

	@Override
	public void input(byte[] in){
		line.write(in, 0, in.length);
	}

	public void close(){
		line.close();
	}

	@Override
	public AudioMessageProcessor get(){
		return this;
	}

	private static AudioFormat getFormat(){
		float sampleRate = 44100;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = false;
		final AudioFormat format =
				new AudioFormat(sampleRate, sampleSizeInBits,
						channels, signed, bigEndian);
		return format;
	}
}
