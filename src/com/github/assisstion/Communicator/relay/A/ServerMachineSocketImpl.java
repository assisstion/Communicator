package com.github.assisstion.Communicator.relay.A;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;

import com.github.assisstion.Communicator.relay.ServerMachineSocket;


public class ServerMachineSocketImpl implements ServerMachineSocket{

	protected ServerSocket socket;

	public ServerMachineSocketImpl(ServerSocket socket){
		this.socket = socket;
	}

	@Override
	public ServerSocket get(){
		return socket;
	}

	@Override
	public void close() throws IOException{
		socket.close();
	}

	@Override
	public void bind(SocketAddress bindpoint) throws IOException{
		socket.bind(bindpoint);
	}

	@Override
	public InetAddress getInetAddress(){
		return socket.getInetAddress();
	}

	@Override
	public int getLocalPort(){
		return socket.getLocalPort();
	}

	@Override
	public InetAddress getLocalAddress(){
		return socket.getInetAddress();
	}

	@Override
	public SocketAddress getLocalSocketAddress(){
		return socket.getLocalSocketAddress();
	}

	@Override
	public int getSoTimeout() throws IOException{
		return socket.getSoTimeout();
	}

	@Override
	public int getReceiveBufferSize() throws SocketException{
		return socket.getReceiveBufferSize();
	}

	@Override
	public boolean getReuseAddress() throws SocketException{
		return socket.getReuseAddress();
	}

	@Override
	public boolean isBound(){
		return socket.isBound();
	}

	@Override
	public boolean isClosed(){
		return socket.isClosed();
	}

	@Override
	public void setPerformancePreferences(int connectionTime, int latency,
			int bandwidth){
		socket.setPerformancePreferences(connectionTime, latency, bandwidth);
	}

	@Override
	public void setReceiveBufferSize(int size) throws SocketException{
		socket.setReceiveBufferSize(size);
	}

	@Override
	public void setReuseAddress(boolean on) throws SocketException{
		socket.setReuseAddress(on);
	}

	@Override
	public void setSoTimeout(int timeout) throws SocketException{
		socket.setSoTimeout(timeout);
	}

	@Override
	public int getPort(){
		return socket.getLocalPort();
	}

}
