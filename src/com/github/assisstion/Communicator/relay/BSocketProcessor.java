package com.github.assisstion.Communicator.relay;

public interface BSocketProcessor{
	void input(String string);
	void attachHandler(ASocketHandler handler);
}
