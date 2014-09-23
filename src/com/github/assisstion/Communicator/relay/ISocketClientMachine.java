package com.github.assisstion.Communicator.relay;

import java.net.Socket;

public interface ISocketClientMachine<T extends ASocketHandler<?>> extends ISocketMachine{
	Socket getClientSocket();
	T getHandler();
}
