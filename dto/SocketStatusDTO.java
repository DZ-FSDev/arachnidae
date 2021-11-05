package com.dz_fs_dev.arachnidae.dto;

import java.net.InetSocketAddress;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object representing results of a port probe.
 * 
 * @author DZ-FSDev
 * @version 0.0.1
 * @since 16.0.1
 */
public class SocketStatusDTO {
	final @Getter InetSocketAddress SOCKET;
	private @Getter @Setter boolean open;
	private @Getter @Setter long timestamp;
	
	/**
	 * Initializes a new SocketStatusDTO with a specified socket, whether it is open, and
	 * the time since midnight, January 1, 1970 UTC(coordinated universal time) in milliseconds.
	 * 
	 * @param socket The socket this DTO represents.
	 * @param open True if the socket is open.
	 * @param timestamp The time since midnight, January 1, 1970 UTC(coordinated universal time) in milliseconds.
	 */
	public SocketStatusDTO(InetSocketAddress socket, boolean open, long timestamp) {
		SOCKET = socket;
		this.open = open;
		this.timestamp = timestamp;
	}
	
	/**
	 * Initializes a new SocketStatusDTO with a specified socket, and whether it is open.
	 * The DTO will be dated with the time since midnight, January 1, 1970 UTC
	 * (coordinated universal time) in milliseconds.
	 * 
	 * @param socket The socket this DTO represents.
	 * @param open True if the socket is open.
	 */
	public SocketStatusDTO(InetSocketAddress socket, boolean open) {
		this(socket, open, System.currentTimeMillis());
	}
	
	/**
	 * @since 0.0.1
	 */
	@Override
	public int hashCode() {
		final int prime = 57899;
		int result = 1;
		result = prime * result + ((SOCKET == null) ? 0 : SOCKET.hashCode());
		result = prime * result + (open ? 1231 : 1237);
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}
	
	/**
	 * @since 0.0.1
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof SocketStatusDTO))
			return false;
		SocketStatusDTO other = (SocketStatusDTO) obj;
		if (SOCKET == null) {
			if (other.SOCKET != null)
				return false;
		} else if (!SOCKET.equals(other.SOCKET))
			return false;
		if (open != other.open)
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
}
