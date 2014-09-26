/**
 * The com.github.assisstion.Communicator.relay API contains classes to
 * enable programmers to easily connect from one Socket to another and to
 * provide custom functionality associated with the sockets. Each package
 * that is labeled with a letter of the alphabet is a stand-alone package
 * that depends only on packages with a letter closer to the beginning of
 * the alphabet, which all eventually depend on this package. Each package
 * later in the alphabet provide higher-level programming functionality
 * than the previous, allowing programmers to choose feasibility, flexibility,
 * or a mixture of both when working with the API. This API is still being
 * updated, so not all interfaces are in their final form. Feel free to
 * extend any class to provide your own additional functionality.
 *
 * @author Markus Feng
 */
package com.github.assisstion.Communicator.relay;

import java.io.Closeable;

/**
 * Represents a generic SocketMachine. Extend to provide additional functionality.
 * SocketMachines are generally used to handle connections between Sockets.
 *
 * @author Markus Feng
 */
public interface SocketMachine extends Closeable, Runnable{
	/**
	 * Opens the SocketMachine to start all operations.
	 * By convention, one should start the Runnable of the
	 * machine on a different thread before calling open(),
	 * to allow interaction wit the SocketMachine before
	 * open() is called. In most implementations, the Runnable
	 * of the machine is called in the constructor to allow
	 * immediate interaction with the machine once it is created.
	 */
	void open();


	/**
	 * Returns the Socket that is owned by this machine
	 * @return the Socket that is owned by this machine
	 */
	MachineSocket getSocket();
}
