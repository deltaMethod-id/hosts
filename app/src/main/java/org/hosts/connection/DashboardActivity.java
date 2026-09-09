package org.hosts.connection;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.hosts.connection.server.localServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class DashboardActivity extends BaseActivity {
	private TextView statusText;
	private TextView addressText;
	private TextView portText;
	private TextView titleText;
	private Button startStopButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		statusText = findViewById(R.id.dashboard_status);
		addressText = findViewById(R.id.dashboard_address);
		portText = findViewById(R.id.dashboard_port_value);
		titleText = findViewById(R.id.dashboard_title_value);
		startStopButton = findViewById(R.id.dashboard_start_stop);

		Button configButton = findViewById(R.id.dashboard_config);
		Button ipButton = findViewById(R.id.dashboard_ip);
		Button dnsButton = findViewById(R.id.dashboard_dns);
		Button rulesButton = findViewById(R.id.dashboard_rules);

		PreferencesManager prefs = new PreferencesManager(this);
		portText.setText(String.valueOf(prefs.getPort()));
		titleText.setText(prefs.getTitle());

		localServer server = ServerInstance.getServer();
		updateStatus(server);

		startStopButton.setOnClickListener(v -> {
			if (server != null && server.isRunning()) {
				stopServer();
			} else {
				startServer();
			}
		});

		configButton.setOnClickListener(v -> startActivity(new Intent(this, ConfigActivity.class)));
		ipButton.setOnClickListener(v -> startActivity(new Intent(this, IpActivity.class)));
		dnsButton.setOnClickListener(v -> startActivity(new Intent(this, DnsActivity.class)));
		rulesButton.setOnClickListener(v -> startActivity(new Intent(this, RulesActivity.class)));
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_dashboard;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.drawer_dashboard);
	}

	private void updateStatus(localServer server) {
		if (server != null && server.isRunning()) {
			statusText.setText(getString(R.string.dashboard_status_running));
			startStopButton.setText(getString(R.string.dashboard_stop));
			addressText.setText("http://" + getLocalIpAddress() + ":" + server.getPort());
		} else {
			statusText.setText(getString(R.string.dashboard_status_stopped));
			startStopButton.setText(getString(R.string.dashboard_start));
			addressText.setText(getString(R.string.dashboard_no_address));
		}
	}

	private void startServer() {
		PreferencesManager prefs = new PreferencesManager(this);
		int port = prefs.getPort();
		String title = prefs.getTitle();
		localServer server = new localServer(port, title, new localServer.Listener() {
			@Override
			public void onStarted(int startedPort) {
				ServerInstance.setServer(server);
				runOnUiThread(() -> {
					portText.setText(String.valueOf(startedPort));
					updateStatus(server);
				});
			}

			@Override
			public void onError(Exception error) {
				runOnUiThread(() -> {
					Toast.makeText(DashboardActivity.this, getString(R.string.toast_server_failed) + ": " + error.getMessage(), Toast.LENGTH_SHORT).show();
					updateStatus(null);
				});
			}
		});
		server.start();
	}

	private void stopServer() {
		localServer server = ServerInstance.getServer();
		if (server != null) {
			server.stop();
			ServerInstance.setServer(null);
		}
		updateStatus(null);
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

	@Override
	protected void onResume() {
		super.onResume();
		PreferencesManager prefs = new PreferencesManager(this);
		portText.setText(String.valueOf(prefs.getPort()));
		titleText.setText(prefs.getTitle());
		updateStatus(ServerInstance.getServer());
	}
}
