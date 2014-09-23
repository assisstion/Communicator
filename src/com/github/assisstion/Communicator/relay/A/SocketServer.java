package com.github.assisstion.Communicator.relay.A;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.github.assisstion.Communicator.relay.SocketServerMachine;
import com.github.assisstion.Communicator.relay.L.SocketListener;

public class SocketServer<T extends SocketHandler<?>> implements SocketServerMachine<T>{

	protected boolean open;

	protected ServerSocket server;
	protected Set<SocketListener<T>> listeners;
	protected List<T> clients;
	protected SocketHandlerGenerator<T> generator;
	protected boolean started = false;
	protected boolean closed = false;

	protected SocketServer(){
		clients = new LinkedList<T>();
		listeners = new CopyOnWriteArraySet<SocketListener<T>>();
	}

	public SocketServer(ServerSocket socket, SocketHandlerGenerator<T> gen) throws IOException{
		this();
		generator = gen;
		server = socket;
		new Thread(this).start();
	}

	public SocketServer(int port, SocketHandlerGenerator<T> gen) throws IOException{
		this(new ServerSocket(port), gen);
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
					T handler = generator.apply(client);
					clients.add(handler);
					new Thread(handler).start();
					new Thread(new LSocketDispatcher(handler)).start();
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

	@Override
	public ServerSocket getServerSocket(){
		return server;
	}

	@Override
	public SocketHandlerGenerator<T> getHandlerGenerator(){
		return generator;
	}

	@Override
	public List<T> getClientList(){
		return Collections.unmodifiableList(clients);
	}

	@Override
	public Set<SocketListener<T>> getListenerSet(){
		return Collections.unmodifiableSet(listeners);
	}

	@Override
	public void addListener(SocketListener<T> listener){
		listeners.add(listener);
	}

	@Override
	public void addListeners(Set<SocketListener<T>> listeners){
		listeners.addAll(listeners);
	}

	@Override
	public void removeListener(SocketListener<T> listener){
		listeners.remove(listener);
	}

	@Override
	public void removeListeners(Set<SocketListener<T>> listeners){
		listeners.removeAll(listeners);
	}

	protected class LSocketDispatcher implements Runnable{

		protected T handler;

		public LSocketDispatcher(T handler){
			this.handler = handler;
		}

		@Override
		public void run(){
			for(SocketListener<T> listener : listeners){
				listener.accept(handler);
			}
		}

		protected class LSocketAttachRunner implements Runnable{

			protected SocketListener<T> listener;
			protected T handler;

			public LSocketAttachRunner(SocketListener<T> listener, T handler){
				this.listener = listener;
				this.handler = handler;
			}

			@Override
			public void run(){
				listener.accept(handler);
			}
		}
	}
}
