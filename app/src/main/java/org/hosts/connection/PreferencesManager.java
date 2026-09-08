package org.hosts.connection;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
	private static final String PREF_NAME = "hosts_preferences";
	private final SharedPreferences preferences;

	public PreferencesManager(Context context) {
		preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
	}

	// Port configuration
	public int getPort() { return preferences.getInt("port", 8080); }
	public void setPort(int port) { preferences.edit().putInt("port", port).apply(); }

	// Server title
	public String getTitle() { return preferences.getString("title", "hosts"); }
	public void setTitle(String title) { preferences.edit().putString("title", title).apply(); }

	// IP configuration
	public String getIp() { return preferences.getString("ip", "192.168.1.10"); }
	public void setIp(String ip) { preferences.edit().putString("ip", ip).apply(); }

	// DNS configuration
	public String getDnsHostname() { return preferences.getString("dns_hostname", "local.test"); }
	public void setDnsHostname(String hostname) { preferences.edit().putString("dns_hostname", hostname).apply(); }

	public String getDnsAddress() { return preferences.getString("dns_address", "192.168.1.10"); }
	public void setDnsAddress(String address) { preferences.edit().putString("dns_address", address).apply(); }

	// Rules configuration
	public String getRule(int index) { return preferences.getString("rule_" + index, ""); }
	public void setRule(int index, String rule) { preferences.edit().putString("rule_" + index, rule).apply(); }

	public int getRuleCount() { return preferences.getInt("rule_count", 0); }
	public void setRuleCount(int count) { preferences.edit().putInt("rule_count", count).apply(); }

	public void addRule(String rule) {
		int count = getRuleCount();
		setRule(count, rule);
		setRuleCount(count + 1);
	}

	public void removeRule(int index) {
		int count = getRuleCount();
		for (int i = index; i < count - 1; i++) {
			String rule = getRule(i + 1);
			setRule(i, rule);
		}
		setRuleCount(count - 1);
	}

	public void clearAll() { preferences.edit().clear().apply(); }
}
