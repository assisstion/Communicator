package com.github.assisstion.Communicator.relay.B;

import java.io.IOException;
import java.net.Socket;

import com.github.assisstion.Communicator.relay.A.ASocketHandler;

public abstract class BSocketHandlerImplAbstract<T> implements ASocketHandler<T>{

	protected Socket socket;
	protected BSocketProcessor<T> processor;
	protected boolean init = false;
	protected boolean closed = false;
	protected boolean open = false;
	protected boolean started = false;

	public BSocketHandlerImplAbstract(BSocketProcessor<T> processor){
		this.processor = processor;
	}

	public BSocketHandlerImplAbstract(Socket socket, BSocketProcessor<T> processor){
		this(processor);
		openSocket(socket);
	}

	public synchronized void openSocket(Socket socket){
		this.socket = socket;
		processor.attachHandler(this);
		open = true;
		notify();
	}

	@Override
	public void run(){
		try{
			if(started || closed){
				return;
			}
			started = true;
			synchronized(this){
				while(!open){
					try{
						wait();
					}
					catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			initialize();
			synchronized(this){
				init = true;
				notifyAll();
			}
			readFromIn();
		}
		catch(IOException e){
			if(!closed){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally{
			if(!closed){
				try{
					close();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	protected abstract void initialize() throws IOException;

	protected abstract void readFromIn() throws IOException;

	protected abstract void writeToOut(T obj) throws IOException;

	@Override
	public void push(T out) throws IOException{
		while(!init){
			synchronized(this){
				try{
					wait();
				}
				catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(!closed){
			writeToOut(out);
		}
		else{
			throw new IOException("Socket not open");
		}
	}

	@Override
	public void close() throws IOException{
		if(closed){
			return;
		}
		closed = true;
		processor.removeHandler(this);
		socket.close();
	}

	public class Inputtor implements Runnable{

		protected T in;

		public Inputtor(T inputLine){
			in = inputLine;
		}

		@Override
		public void run(){
			processor.input(in);
		}

	}

	@Override
	public Socket getSocket(){
		return socket;
	}

}
