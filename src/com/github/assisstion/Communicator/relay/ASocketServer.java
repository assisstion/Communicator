package com.github.assisstion.Communicator.relay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ASocketServer<T extends ASocketHandler> implements ASocketMachine{

	protected boolean open;

	protected ServerSocket server;
	protected List<T> clients;
	protected ASocketHandlerGenerator<T> generator;
	protected boolean started = false;
	protected boolean closed = false;

	protected ASocketServer(){
		clients = new LinkedList<T>();
	}

	public ASocketServer(int port, ASocketHandlerGenerator<T> gen) throws IOException{
		this();
		generator = gen;
		server = new ServerSocket(port);
		new Thread(this).start();
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
		IOException e = null;
		open = false;
		server.close();
		for(T t : clients){
			try{
				t.close();
			}
			catch(IOException e1){
				e = new IOException(e1);
			}
		}
		if(e != null){
			throw e;
		}
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
					wait();
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			while(open){
				try{
					Socket client = server.accept();
					T handler = generator.generate(client);
					clients.add(handler);
					new Thread(handler).start();
				}
				catch(IOException e){
					if(!closed){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}
}
