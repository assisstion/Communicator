package com.github.assisstion.Communicator.relay;

import java.io.IOException;
import java.util.Set;

public interface BSocketProcessor<T>{
	void input(T in);
	void attachHandler(ASocketHandler<T> handler);
	void removeHandler(ASocketHandler<T> handler);
	Set<ASocketHandler<T>> getHandlers();
	void removeAllHandlers();
	void output(T out, boolean block) throws IOException;
	void outputToHandler(ASocketHandler<T> handler, T out, boolean block) throws IOException;
	boolean isInputBlockingEnabled();
}
