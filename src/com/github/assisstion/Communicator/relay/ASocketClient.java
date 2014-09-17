package com.github.assisstion.Communicator.relay;

import java.io.IOException;
import java.net.Socket;

public class ASocketClient<T extends ASocketHandler> implements ASocketMachine{

	protected boolean open;

	protected Socket client;
	protected ASocketHandler handler;
	protected boolean started = false;
	protected boolean closed = false;

	protected ASocketClient(){

	}

	public ASocketClient(Socket socket, ASocketHandler handler) throws IOException{
		this();
		this.handler = handler;
		client = socket;
		new Thread(this).start();
	}

	public ASocketClient(String host, int port, ASocketHandler handler) throws IOException{
		this(new Socket(host, port), handler);
	}

	public Socket getClientSocket(){
		return client;
	}

	@Override
	public synchronized void open(){
		open = true;
		notify();
	}

	@Override
	public void close() throws IOException{
		if(closed){
			return;
		}
		closed = true;
		if(!started){
			return;
		}
		open = false;
		handler.close();
	}

	@Override
	public void run(){
		synchronized(this){
			if(started){
				return;
			}
			else{
				started = true;
			}
			while(!open){
				try{
					this.wait();
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			new Thread(handler).start();
		}

	}
}
