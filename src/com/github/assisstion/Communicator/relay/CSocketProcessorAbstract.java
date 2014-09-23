package com.github.assisstion.Communicator.relay;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class CSocketProcessorAbstract<T> implements BSocketProcessor<T>{

	protected Set<ASocketHandler<T>> handlers = new HashSet<ASocketHandler<T>>();

	@Override
	public void attachHandler(ASocketHandler<T> handler){
		handlers.add(handler);
	}

	@Override
	public void removeHandler(ASocketHandler<T> handler){
		handlers.remove(handler);
	}

	@Override
	public Set<ASocketHandler<T>> getHandlers(){
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
	public void outputToHandler(ASocketHandler<T> handler, T out, boolean block) throws IOException{
		if(block){
			new Outputter(handler, out).output();
		}
		else{
			new Thread(new Outputter(handler, out)).start();
		}
	}

	public class Outputter implements Runnable{

		protected T text = null;
		protected ASocketHandler<T> out = null;

		public Outputter(ASocketHandler<T> handler, T string){
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
				for(ASocketHandler<T> handler : handlers){
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
