package org.hosts.connection.config;

public final class dns {
	private String hostname;
	private String address;

	public dns(String hostname, String address) {
		setHostname(hostname);
		setAddress(address);
	}

	public String getHostname() { return hostname; }
	public String getAddress() { return address; }

	public void setHostname(String hostname) {
		if (hostname == null || hostname.trim().isEmpty()) throw new IllegalArgumentException("Nama host wajib diisi");
		this.hostname = hostname.trim();
	}

	public void setAddress(String address) {
		if (address == null || address.trim().isEmpty()) throw new IllegalArgumentException("Alamat IP wajib diisi");
		this.address = address.trim();
	}
}
