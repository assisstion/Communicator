package com.github.assisstion.Communicator.relay;

import java.io.IOException;

public interface BSocketProcessor{
	void input(String string);
	void attachHandler(ASocketHandler handler);
	void output(String out) throws IOException;
}
