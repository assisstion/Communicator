package com.github.assisstion.Communicator.relay.C;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.github.assisstion.Communicator.relay.A.SocketHandler;
import com.github.assisstion.Communicator.relay.B.SocketProcessor;

public abstract class SocketProcessorAbstract<T> implements SocketProcessor<T>{

	protected Set<SocketHandler<T>> handlers = new HashSet<SocketHandler<T>>();

	@Override
	public void attachHandler(SocketHandler<T> handler){
		handlers.add(handler);
	}

	@Override
	public void removeHandler(SocketHandler<T> handler){
		handlers.remove(handler);
	}

	@Override
	public Set<SocketHandler<T>> getHandlers(){
		return Collections.unmodifiableSet(handlers);
	}

	@Override
	public void removeAllHandlers(){
		handlers.clear();
	}

	@Override
	public void output(T out, boolean block) throws IOException{
		if(block){
			new Outputter(null, out).output();
		}
		else{
			new Thread(new Outputter(null, out)).start();
		}
	}

	@Override
	public void outputToHandler(SocketHandler<T> handler, T out, boolean block) throws IOException{
		if(block){
			new Outputter(handler, out).output();
		}
		else{
			new Thread(new Outputter(handler, out)).start();
		}
	}

	public class Outputter implements Runnable{

		protected T text = null;
		protected SocketHandler<T> out = null;

		public Outputter(SocketHandler<T> handler, T string){
			out = handler;
			text = string;
		}

		@Override
		public void run(){
			try{
				output();
			}
			catch(IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void output() throws IOException{
			IOException tempException = null;
			if(out == null){
				for(SocketHandler<T> handler : handlers){
					try{
						handler.push(text);
					}
					catch(IOException e){
						tempException = e;
					}
				}
			}
			else{
				try{
					out.push(text);
				}
				catch(IOException e){
					tempException = e;
				}
			}
			if(tempException != null){
				throw new IOException(tempException);
			}
		}
	}

	@Override
	public boolean isInputBlockingEnabled(){
		//Does not block on input
		return false;
	}
}