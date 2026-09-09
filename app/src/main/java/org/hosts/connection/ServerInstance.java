package org.hosts.connection;

import org.hosts.connection.server.localServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ServerInstance {
	private static localServer server;
	private static final List<String> logs = new ArrayList<>();

	public static synchronized void setServer(localServer value) {
		server = value;
		if (server != null) {
			server.addLogListener(logs::add);
		}
	}

	public static synchronized localServer getServer() {
		return server;
	}

	public static synchronized List<String> getLogs() {
		return Collections.unmodifiableList(logs);
	}

	public static synchronized void clearLogs() {
		logs.clear();
	}
}
