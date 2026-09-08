package org.hosts.connection.tools;

import org.hosts.connection.server.localServer;

public final class startServer {
	private startServer() { }

	public static localServer start(int port, localServer.Listener listener) {
		if (!checkPort.isValid(port)) throw new IllegalArgumentException("Port tidak valid");
		localServer server = new localServer(port, listener);
		server.start();
		return server;
	}
}
