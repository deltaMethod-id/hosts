package org.hosts.connection.tools;

public final class addPort {
	private addPort() { }

	public static int parse(String value) {
		try {
			int port = Integer.parseInt(value == null ? "" : value.trim());
			if (!checkPort.isValid(port)) throw new IllegalArgumentException("Port harus di antara 1024 dan 65535");
			return port;
		} catch (NumberFormatException error) {
			throw new IllegalArgumentException("Port harus berupa angka", error);
		}
	}
}
