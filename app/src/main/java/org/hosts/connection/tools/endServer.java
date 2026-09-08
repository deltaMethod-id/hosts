package org.hosts.connection.tools;

import org.hosts.connection.server.localServer;

public final class endServer {
	private endServer() { }

	public static void stop(localServer server) {
		if (server != null) server.stop();
	}
}
