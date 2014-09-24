package com.github.assisstion.Communicator.relay;

import java.net.ServerSocket;

public interface ServerMachineSocket extends MachineSocket{
	@Override
	ServerSocket get();
}
