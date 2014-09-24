package com.github.assisstion.Communicator.relay;

import java.net.Socket;

public interface ClientMachineSocket extends MachineSocket{
	@Override
	Socket get();
}
