package org.hosts.connection.server;

public final class serverConfig {
	private int port = 8080;
	private String title = "hosts";

	public int getPort() { return port; }
	public String getTitle() { return title; }

	public void setPort(int port) {
		if (port < 1024 || port > 65535) throw new IllegalArgumentException("Port harus di antara 1024 dan 65535");
		this.port = port;
	}

	public void setTitle(String title) {
		if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Judul wajib diisi");
		this.title = title.trim();
	}
}
