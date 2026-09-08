package org.hosts.connection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
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

public class MainActivity extends Activity {
	private EditText portInput;
	private Button serverButton;
	private TextView statusText;
	private TextView addressText;
	private localServer server;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		portInput = findViewById(R.id.port_input);
		serverButton = findViewById(R.id.server_button);
		statusText = findViewById(R.id.status_text);
		addressText = findViewById(R.id.address_text);
		serverButton.setOnClickListener(view -> toggleServer());
	}

	private void toggleServer() {
		if (server != null && server.isRunning()) {
			endServer.stop(server);
			server = null;
			statusText.setText("Server berhenti");
			addressText.setText("");
			serverButton.setText("Mulai server");
			return;
		}

		int port;
		try {
			port = addPort.parse(portInput.getText().toString());
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		server = new localServer(port, new localServer.Listener() {
			@Override
			public void onStarted(int startedPort) {
				runOnUiThread(() -> {
					statusText.setText("Server aktif");
					addressText.setText("http://" + getLocalIpAddress() + ":" + startedPort);
					serverButton.setText("Hentikan server");
				});
			}

			@Override
			public void onError(Exception error) {
				runOnUiThread(() -> {
					server = null;
					statusText.setText("Server gagal dijalankan");
					addressText.setText(error.getMessage());
					serverButton.setText("Mulai server");
				});
			}
		});
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
