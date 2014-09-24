package com.github.assisstion.Communicator.message;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import com.github.assisstion.Communicator.relay.B.SocketProcessorGenerator;
import com.github.assisstion.Communicator.relay.C.SocketProcessorAbstract;


public class AudioMessageProcessor extends SocketProcessorAbstract<byte[]> implements SocketProcessorGenerator<AudioMessageProcessor>, Runnable{

	public static final int BUFFER_SIZE = 1024;

	protected AudioFormat format;
	protected SourceDataLine line;
	protected boolean enabled = false;
	protected boolean started;

	public AudioMessageProcessor() throws LineUnavailableException{
		format = getFormat();
		DataLine.Info info = new DataLine.Info(
				SourceDataLine.class, format);
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		new Thread(this).start();
	}

	public void setEnabled(boolean en){
		if(enabled == en){
			return;
		}
		enabled = en;
		if(en == true){
			synchronized(this){
				notify();
			}
		}
	}

	public boolean isEnabled(){
		return enabled;
	}

	@Override
	public void run(){
		try{
			synchronized(this){
				if(started){
					return;
				}
				else{
					started = true;
				}
				while(!isEnabled()){
					try{
						wait();
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
			AudioFormat format = AudioMessageProcessor.getFormat();
			DataLine.Info info = new DataLine.Info(
					TargetDataLine.class, format);
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			write(new AudioInputStream(line), false);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public synchronized void write(AudioInputStream in, boolean outblocking) throws IOException{
		byte[] buf = new byte[BUFFER_SIZE];
		int count = in.read(buf);
		while(count >= 0){
			while(!isEnabled()){
				try{
					wait();
				}
				catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			byte[] outArr = new byte[count];
			System.arraycopy(buf, 0, outArr, 0, count);
			output(outArr, outblocking);
			count = in.read(buf);
		}
	}

	@Override
	public void input(byte[] in){
		if(isEnabled()){
			line.write(in, 0, in.length);
		}
	}

	public void close(){
		line.close();
	}

	@Override
	public AudioMessageProcessor get(){
		return this;
	}

	public static AudioFormat getFormat(){
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
