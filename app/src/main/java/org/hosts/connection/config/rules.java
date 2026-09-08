package org.hosts.connection.config;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public final class rules {
	private final Set<String> allowedHosts = new LinkedHashSet<>();

	public void allow(String host) {
		if (host == null || host.trim().isEmpty()) throw new IllegalArgumentException("Host wajib diisi");
		allowedHosts.add(host.trim());
	}

	public void deny(String host) { allowedHosts.remove(host); }

	public boolean isAllowed(String host) { return allowedHosts.contains(host); }

	public Set<String> getAllowedHosts() { return Collections.unmodifiableSet(allowedHosts); }
}
