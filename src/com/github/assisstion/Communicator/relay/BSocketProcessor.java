package com.github.assisstion.Communicator.relay;

import java.io.IOException;
import java.util.Set;

public interface BSocketProcessor{
	void input(String string);
	void attachHandler(ASocketHandler handler);
	void removeHandler(ASocketHandler handler);
	Set<ASocketHandler> getHandlers();
	void removeAllHandlers();
	void output(String out, boolean block) throws IOException;
	void outputToHandler(ASocketHandler handler, String out, boolean block) throws IOException;
}
