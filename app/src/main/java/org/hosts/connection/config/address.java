package org.hosts.connection.config;

public final class address {
	private final String host;
	private final int port;

	public address(String host, int port) {
		if (host == null || host.trim().isEmpty()) throw new IllegalArgumentException("Host wajib diisi");
		if (port < 1 || port > 65535) throw new IllegalArgumentException("Port tidak valid");
		this.host = host.trim();
		this.port = port;
	}

	public String getHost() { return host; }
	public int getPort() { return port; }
	public String asUrl() { return "http://" + host + ":" + port; }
}
