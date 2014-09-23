package com.github.assisstion.Communicator.relay.Stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.github.assisstion.Communicator.relay.B.BSocketHandlerImplAbstract;
import com.github.assisstion.Communicator.relay.B.BSocketProcessor;
import com.github.assisstion.Communicator.relay.B.BSocketHandlerImplAbstract.Inputtor;

public class SSocketStreamHandlerImpl extends BSocketHandlerImplAbstract<byte[]> {
	public static final int DEFAULT_BUFFER_SIZE = 1024;

	protected BufferedOutputStream out;
	protected BufferedInputStream in;
	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	public SSocketStreamHandlerImpl(BSocketProcessor<byte[]> processor){
		super(processor);
	}

	public SSocketStreamHandlerImpl(Socket socket, BSocketProcessor<byte[]> processor){
		super(socket, processor);
	}

	public SSocketStreamHandlerImpl(BSocketProcessor<byte[]> processor, int bufferSize){
		super(processor);
		this.bufferSize = bufferSize;
	}

	public SSocketStreamHandlerImpl(Socket socket, BSocketProcessor<byte[]> processor, int bufferSize){
		super(socket, processor);
		this.bufferSize = bufferSize;
	}

	@Override
	public void initialize() throws IOException{
		out = new BufferedOutputStream(socket.getOutputStream());
		in = new BufferedInputStream(socket.getInputStream());
	}

	@Override
	public void readFromIn() throws IOException{

		byte[] buffer = new byte[bufferSize];
		int count = in.read(buffer);
		while (count >= 0) {
			if(!closed){
				try{
					byte[] byteHolder = new byte[count];
					System.arraycopy(buffer, 0, byteHolder, 0, count);
					if(!processor.isInputBlockingEnabled()){
						new Thread(new Inputtor(byteHolder)).start();
					}
					else{
						new Inputtor(byteHolder).run();
					}
				}
				catch(Exception e){
					if(!closed){
						e.printStackTrace();
					}
				}
				count = in.read(buffer);
			}
		}
	}

	//Up to user to make sure out is not modified
	//Make new arrays per push
	@Override
	public void writeToOut(byte[] arr) throws IOException{
		out.write(arr);
	}

}
