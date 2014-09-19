package com.github.assisstion.Communicator.relay;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class CSocketProcessorAbstract implements BSocketProcessor{

	protected Set<ASocketHandler> handlers = new HashSet<ASocketHandler>();

	@Override
	public void attachHandler(ASocketHandler handler){
		handlers.add(handler);
	}

	@Override
	public void removeHandler(ASocketHandler handler){
		handlers.remove(handler);
	}

	@Override
	public Set<ASocketHandler> getHandlers(){
		return Collections.unmodifiableSet(handlers);
	}

	@Override
	public void removeAllHandlers(){
		handlers.clear();
	}

	@Override
	public void output(String out, boolean block) throws IOException{
		if(block){
			new Outputter(null, out).output();
		}
		else{
			new Thread(new Outputter(null, out)).start();
		}
	}

	@Override
	public void outputToHandler(ASocketHandler handler, String out, boolean block) throws IOException{
		if(block){
			new Outputter(handler, out).output();
		}
		else{
			new Thread(new Outputter(handler, out)).start();
		}
	}

	public class Outputter implements Runnable{

		protected String text = "";
		protected ASocketHandler out = null;

		public Outputter(ASocketHandler handler, String string){
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
				for(ASocketHandler handler : handlers){
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
}
