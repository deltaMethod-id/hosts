package org.hosts.connection.config;

public final class ip {
	private final String value;

	public ip(String value) {
		if (!isValid(value)) throw new IllegalArgumentException("Format IP tidak valid");
		this.value = value;
	}

	public String getValue() { return value; }

	public static boolean isValid(String value) {
		if (value == null) return false;
		String[] parts = value.trim().split("\\.", -1);
		if (parts.length != 4) return false;
		for (String part : parts) {
			try {
				if (part.isEmpty() || Integer.parseInt(part) < 0 || Integer.parseInt(part) > 255) return false;
			} catch (NumberFormatException error) {
				return false;
			}
		}
		return true;
	}
}
