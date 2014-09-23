package com.github.assisstion.Communicator.relay.B;

import java.io.IOException;
import java.util.Set;

import com.github.assisstion.Communicator.relay.A.SocketHandler;

public interface SocketProcessor<T>{
	void input(T in);
	void attachHandler(SocketHandler<T> handler);
	void removeHandler(SocketHandler<T> handler);
	Set<SocketHandler<T>> getHandlers();
	void removeAllHandlers();
	void output(T out, boolean block) throws IOException;
	void outputToHandler(SocketHandler<T> handler, T out, boolean block) throws IOException;
	boolean isInputBlockingEnabled();
}
