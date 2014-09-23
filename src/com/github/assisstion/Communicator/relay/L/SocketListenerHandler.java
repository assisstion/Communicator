package com.github.assisstion.Communicator.relay.L;

import java.util.Set;

import com.github.assisstion.Communicator.relay.A.SocketHandler;

public interface SocketListenerHandler<T extends SocketHandler<?>>{
	Set<SocketListener<T>> getListenerSet();
	void addListener(SocketListener<T> listener);
	void addListeners(Set<SocketListener<T>> listeners);
	void removeListener(SocketListener<T> listener);
	void removeListeners(Set<SocketListener<T>> listeners);
}
