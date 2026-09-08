package org.hosts.connection.server;

public final class serverRules {
	public boolean isValidPort(int port) { return port >= 1024 && port <= 65535; }

	public void requireValidPort(int port) {
		if (!isValidPort(port)) throw new IllegalArgumentException("Port harus di antara 1024 dan 65535");
	}
}
