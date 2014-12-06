package com.github.assisstion.Communicator.message;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import com.markusfeng.SocketRelay.B.SocketProcessorGenerator;
import com.markusfeng.SocketRelay.C.SocketProcessorAbstract;


public class AudioMessageProcessor extends SocketProcessorAbstract<byte[]> implements SocketProcessorGenerator<AudioMessageProcessor>, Runnable{

	public static final int BUFFER_SIZE = 1024;
	public static final int AUDIO_BUFFER_SIZE = 65536;

	public static final boolean ENABLE_EXTRP = true;

	protected AudioOutProcessor aop;
	protected AudioInProcessor aip;
	protected AudioFormat format;
	protected SourceDataLine line;
	protected boolean enabled = false;
	protected boolean started;

	private boolean closed = false;

	public AudioMessageProcessor() throws LineUnavailableException{
		format = getFormat();
		DataLine.Info info = new DataLine.Info(
				SourceDataLine.class, format);
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		aop = new AudioOutProcessor();
		aip = new AudioInProcessor();
		new Thread(this).start();
		new Thread(aop).start();
		new Thread(aip).start();
	}

	public void setEnabled(boolean en){
		if(closed || enabled == en){
			return;
		}
		enabled = en;
		if(en == true){
			synchronized(this){
				notifyAll();
			}
		}
	}

	public boolean isEnabled(){
		return closed ? false : enabled;
	}

	@Override
	public void run(){
		if(closed){
			return;
		}
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
			write(new AudioInputStream(line));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public synchronized void write(AudioInputStream in) throws IOException{
		if(closed){
			return;
		}
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
			if(ENABLE_EXTRP){
				aop.push(outArr);
			}
			else{
				output(outArr, false);
			}
			count = in.read(buf);
		}
	}

	@Override
	public void input(byte[] in){
		if(isEnabled()){
			if(ENABLE_EXTRP){
				aip.push(in);
			}
			else{
				line.write(in, 0, in.length);
			}
		}
	}

	@Override
	public void close(){
		if(!closed){
			closed = true;
			line.close();
		}
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

	protected class AudioOutProcessor implements Runnable{

		protected byte[] backBuffer = new byte[AUDIO_BUFFER_SIZE];
		protected byte[] outBuffer = new byte[AUDIO_BUFFER_SIZE];
		protected int outBufferPosition = 0;

		protected ExecutorService tpe;

		public AudioOutProcessor(){
			tpe = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1024));
			//tpe = Executors.newCachedThreadPool();
		}

		protected class AudioOutProcessorPusher implements Runnable{



			protected byte[] bytes;

			public AudioOutProcessorPusher(byte[] bytes){
				this.bytes = bytes;
			}

			@Override
			public void run(){
				synchronized(outBuffer){
					int outBufferLength = outBuffer.length;
					int bytesLength = bytes.length;
					if(outBufferPosition + bytesLength <= outBufferLength){
						System.arraycopy(bytes, 0, outBuffer, outBufferPosition, bytesLength);
						outBufferPosition = outBufferPosition + bytesLength;
					}
					else{
						if(bytesLength % outBufferLength == 0){
							if(bytesLength == outBufferLength){
								compressToFitScale(new byte[][]{outBuffer, bytes});
							}
							else{
								int numArrays = bytesLength / outBufferLength;
								byte[][] arr = new byte[numArrays + 1][outBufferLength];
								arr[0] = outBuffer;
								for(int i = 0; i < bytesLength / outBufferLength; i++){
									byte[] newArr = new byte[outBufferLength];
									System.arraycopy(bytes, numArrays * outBufferLength, newArr, 0, outBufferLength);
									arr[i+1] = newArr;
								}
								compressToFitScale(arr);
							}
						}
						else{
							compressToFit(bytes);
						}
						outBufferPosition = outBufferLength;
					}
				}
				synchronized(AudioOutProcessor.this){
					AudioOutProcessor.this.notify();
				}
			}
		}

		protected void push(byte[] bytes){
			tpe.execute(new AudioOutProcessorPusher(bytes));
		}

		@Override
		public void run(){
			try{
				// TODO Auto-generated method stub
				while(!closed){
					while(!(outBufferPosition == outBuffer.length)){
						synchronized(AudioOutProcessor.this){
							try{
								AudioOutProcessor.this.wait();
							}
							catch(InterruptedException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					synchronized(outBuffer){
						output(outBuffer, true);
						outBufferPosition = 0;
					}
				}
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}


		protected void compressToFit(byte[] in){
			int l1 = outBuffer.length;
			int l2 = in.length;
			int pos = outBufferPosition;
			int lm = pos + l2;
			byte[] store = new byte[lm];
			System.arraycopy(outBuffer, 0, store, 0, pos);
			System.arraycopy(in, 0, store, pos, l2);
			int bq = byteQuantum();
			for(int i = 0; i < lm / bq; i += bq){
				int art = i * l1 / (lm * bq) * bq;
				for(int j = 0; j < bq; j++){
					outBuffer[art + j] = store[i + j];
				}
			}
		}

		protected void compressToFitScale(byte[][] buffers){
			byte[] outBuffer = buffers[0];
			int ol = outBuffer.length;
			int count = buffers.length;
			int bq = byteQuantum();
			for(int i = 0; i < ol / bq; i += bq){
				int num = count * i / ol;
				int ord = count * i % ol;
				for(int j = 0; j < bq; j++){
					outBuffer[i + j] = buffers[num][ord + j];
				}
			}
		}

	}

	public class AudioInProcessor implements Runnable{

		private static final double EXTRAPOLATION_ERROR = 1.00000001;
		//1 s delay per frame
		private static final long SYSTEM_TIME = 1000000000;
		private static final double EXTRAPOLATE_MAX = 4;
		protected byte[] writeBuffer;
		protected byte[] inBuffer = new byte[AUDIO_BUFFER_SIZE];
		protected byte[] inBufferTemp = null;
		protected int inBufferPosition = 0;
		protected double extrapolateNext = 1;
		protected long pushWaitTime = 0;


		protected ExecutorService tpe;

		public AudioInProcessor(){
			tpe = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1024));
			//tpe = Executors.newCachedThreadPool();
		}

		@Override
		public void run(){
			long lastStart = Long.MIN_VALUE;
			long diff = 0;
			while(!closed){
				while(inBufferPosition != inBuffer.length){
					synchronized(AudioInProcessor.this){
						try{
							AudioInProcessor.this.wait();
						}
						catch(InterruptedException e){
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				if(lastStart != Long.MIN_VALUE){
					long megDiff = System.nanoTime() - lastStart;
					extrapolateNext = (megDiff - pushWaitTime) /
							((diff + SYSTEM_TIME) * EXTRAPOLATION_ERROR);
					extrapolateNext = extrapolateNext > EXTRAPOLATE_MAX ? EXTRAPOLATE_MAX : extrapolateNext;
					//System.out.println(extrapolateNext);
				}
				synchronized(AudioInProcessor.this){
					lastStart = System.nanoTime();
					line.write(writeBuffer, 0, writeBuffer.length);
					diff = System.nanoTime() - lastStart;
					//System.out.println(diff);
					inBufferPosition = 0;
				}
			}
		}

		public class AudioInProcessorPusher implements Runnable{

			protected byte[] inX;

			public AudioInProcessorPusher(byte[] in){
				inX = in;
			}

			@Override
			public void run(){
				long startPush = System.nanoTime();
				synchronized(AudioInProcessor.this){
					mesh(inX);
					pushWaitTime = System.nanoTime() - startPush;
					if(extrapolateNext > 1){
						extrapolate();
					}
					else{
						writeBuffer = inBuffer;
						extrapolateNext = 1;
					}
					AudioInProcessor.this.notify();
				}
			}

		}

		private void mesh(byte[] inX2){
			if(inBufferTemp != null){
				System.arraycopy(inBufferTemp, 0, inBuffer, 0, inBufferTemp.length);
				inBufferTemp = null;
			}
			if(inBufferPosition + inX2.length <= inBuffer.length){
				System.arraycopy(inX2, 0, inBuffer, inBufferPosition, inX2.length);
				inBufferPosition += inX2.length;
			}
			else{
				System.arraycopy(inX2, 0, inBuffer, inBufferPosition, inBuffer.length - inBufferPosition);
				inBufferTemp = new byte[inX2.length + inBufferPosition - inBuffer.length];
				System.arraycopy(inX2, inBuffer.length - inBufferPosition, inBufferTemp, 0, inBufferTemp.length);

				inBufferPosition = inBuffer.length;
			}
		}


		public void push(byte[] in){
			tpe.execute(new AudioInProcessorPusher(in));
		}

		protected void extrapolate(){
			int bq = byteQuantum();
			int outO = (int) (inBuffer.length * extrapolateNext / bq);
			int out = outO * bq;
			byte[] inNew = new byte[out];
			for(int i = 0; i < outO / bq; i += bq){
				int art = i * inBuffer.length / (out * bq) * bq;
				for(int j = 0; j < bq; j++){
					inNew[i + j] = inBuffer[art + j];
				}
			}
			writeBuffer = inNew;
		}

	}

	protected int byteQuantum(){
		return format.getFrameSize();
	}
}
