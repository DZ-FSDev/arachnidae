package com.dz_fs_dev.arachnidae.toolkit;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Various TCP/IP tools.
 * 
 * @author DZ-FSDev
 * @since 16.0.1
 * @version 0.0.1
 */
public final class SocketTools {
	/**
	 * Returns true if a connection can be made to a specified socket.
	 * A timeout of 0 is treated as infinite.
	 * 
	 * @param ipHost The IPv4 address or hostname to test.
	 * @param port The port to be tested.
	 * @param timeout The timeout of the connection attempt.
	 * @return True if a connection can be made to a specified socket.
	 */
	public static boolean isSocketOpen(String ipHost, int port, int timeout) {
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(ipHost, port), timeout);
			socket.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * Returns -1 if a series of connections to sockets is successful. On
	 * the first connection failure, returns the specified port that failed.
	 * A timeout of 0 is treated as infinite. 
	 * 
	 * @param ipHost The IPv4 address or hostname to test.
	 * @param ports The array of ports to be tested.
	 * @param timeout The timeout of each connection attempt.
	 * @return -1 if all connections can be made to a specified socket or
	 * 			the specified port that failed to connect.
	 */
	public static int isSocketOpen(String ipHost, int[] ports, int timeout) {
		int failedOn = -1;
		for(int port : ports) {
			if(failedOn == -1 && !isSocketOpen(ipHost, port, timeout)) {
				failedOn = port;
			}
		}

		return failedOn;
	}
}
