package com.github.assisstion.Communicator.relay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BSocketHandlerImpl implements ASocketHandler{

	protected Socket socket;
	protected BSocketProcessor processor;
	protected boolean init = false;
	protected boolean closed = false;
	protected PrintWriter out;
	protected BufferedReader in;
	protected boolean open = false;
	protected boolean started = false;

	public BSocketHandlerImpl(BSocketProcessor processor){
		this.processor = processor;
	}

	public BSocketHandlerImpl(Socket socket, BSocketProcessor processor){
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
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));

			synchronized(this){
				init = true;
				notifyAll();
			}
			String inputLine;

			while (!closed && (inputLine = in.readLine()) != null) {
				if(!closed){
					try{
						new Thread(new Inputtor(inputLine)).start();
					}
					catch(Exception e){
						if(!closed){
							e.printStackTrace();
						}
					}
				}
			}
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

	@Override
	public void push(String out) throws IOException{
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
			this.out.println(out);
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

		protected String in;

		public Inputtor(String inputLine){
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
