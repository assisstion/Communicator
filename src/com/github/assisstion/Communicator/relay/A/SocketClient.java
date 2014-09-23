package com.github.assisstion.Communicator.relay.A;

import java.io.IOException;
import java.net.Socket;

import com.github.assisstion.Communicator.relay.SocketClientMachine;

public class SocketClient<T extends SocketHandler<?>> implements SocketClientMachine<T>{

	protected boolean open;

	protected Socket client;
	protected T handler;
	protected boolean started = false;
	protected boolean closed = false;

	protected SocketClient(){

	}

	public SocketClient(Socket socket, T handler) throws IOException{
		this();
		this.handler = handler;
		client = socket;
		new Thread(this).start();
	}

	public SocketClient(String host, int port, T handler) throws IOException{
		this(new Socket(host, port), handler);
	}

	@Override
	public T getHandler(){
		return handler;
	}

	@Override
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