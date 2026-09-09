package org.hosts.connection;

import android.os.Bundle;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class AddressActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView addressText = findViewById(R.id.server_address);
		org.hosts.connection.server.localServer server = ServerInstance.getServer();
		if (server != null && server.isRunning()) {
			addressText.setText("http://" + getLocalIpAddress() + ":" + server.getPort());
		} else {
			addressText.setText(getString(R.string.address_not_started));
		}
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_address;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.address_title);
	}

	private String getLocalIpAddress() {
		try {
			java.util.Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				java.util.Enumeration<java.net.InetAddress> addresses = interfaces.nextElement().getInetAddresses();
				while (addresses.hasMoreElements()) {
					java.net.InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress() && address instanceof java.net.Inet4Address) {
						return address.getHostAddress();
					}
				}
			}
		} catch (Exception ignored) {
		}
		return "127.0.0.1";
	}
}
