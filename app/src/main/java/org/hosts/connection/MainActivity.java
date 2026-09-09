package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.hosts.connection.server.localServer;
import org.hosts.connection.tools.addPort;
import org.hosts.connection.tools.endServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MainActivity extends BaseActivity {
	private EditText portInput;
	private Button serverButton;
	private TextView statusText;
	private TextView addressText;
	private localServer server;
	private android.content.SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = getSharedPreferences("server_preferences", MODE_PRIVATE);

		portInput = findViewById(R.id.port_input);
		serverButton = findViewById(R.id.server_button);
		statusText = findViewById(R.id.status_text);
		addressText = findViewById(R.id.address_text);

		portInput.setText(String.valueOf(preferences.getInt("port", 8080)));
		serverButton.setOnClickListener(view -> toggleServer());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_main;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.menu_server);
	}

	private void toggleServer() {
		if (server != null && server.isRunning()) {
			endServer.stop(server);
			server = null;
			statusText.setText(getString(R.string.server_stopped));
			addressText.setText("");
			serverButton.setText(getString(R.string.server_start));
			return;
		}

		int port;
		try {
			port = addPort.parse(portInput.getText().toString());
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		String title = preferences.getString("title", "hosts");
		preferences.edit().putInt("port", port).apply();
		server = new localServer(port, title, new localServer.Listener() {
			@Override
			public void onStarted(int startedPort) {
				runOnUiThread(() -> {
					statusText.setText(getString(R.string.server_running));
					addressText.setText("http://" + getLocalIpAddress() + ":" + startedPort);
					serverButton.setText(getString(R.string.server_stop));
				});
			}

			@Override
			public void onError(Exception error) {
				runOnUiThread(() -> {
					statusText.setText(getString(R.string.toast_server_failed));
					addressText.setText(error.getMessage());
					serverButton.setText(getString(R.string.server_start));
				});
			}
		});
		ServerInstance.setServer(server);
		server.start();
	}

	private String getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
						return address.getHostAddress();
					}
				}
			}
		} catch (Exception ignored) {
		}
		return "127.0.0.1";
	}

	@Override
	protected void onDestroy() {
		endServer.stop(server);
		super.onDestroy();
	}
}
