package org.hosts.connection.tools;

import java.io.IOException;
import java.net.ServerSocket;

public final class checkPort {
	private checkPort() { }

	public static boolean isValid(int port) { return port >= 1024 && port <= 65535; }

	public static boolean isAvailable(int port) {
		if (!isValid(port)) return false;
		try (ServerSocket socket = new ServerSocket(port)) {
			return true;
		} catch (IOException error) {
			return false;
		}
	}
}
