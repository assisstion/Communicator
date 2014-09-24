package com.github.assisstion.Communicator.relay;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

public interface MachineSocket extends Closeable{
	Object get();
	void bind(SocketAddress bindpoint) throws IOException;
	InetAddress getInetAddress();
	int getLocalPort();
	InetAddress getLocalAddress();
	SocketAddress getLocalSocketAddress();
	int getSoTimeout() throws IOException;
	int getReceiveBufferSize() throws SocketException;
	boolean getReuseAddress() throws SocketException;
	int getPort();
	boolean isBound();
	boolean isClosed();
	void setPerformancePreferences(int connectionTime, int latency, int bandwidth);
	void setReceiveBufferSize(int size) throws SocketException;
	void setReuseAddress(boolean on) throws SocketException;
	void setSoTimeout(int timeout) throws SocketException;
}
