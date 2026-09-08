package org.hosts.connection.config;

public final class name {
	private String value;

	public name(String value) { setValue(value); }

	public String getValue() { return value; }

	public void setValue(String value) {
		if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Nama wajib diisi");
		this.value = value.trim();
	}
}
