package org.hosts.connection;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
	private android.content.SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		portInput = findViewById(R.id.port_input);
		serverButton = findViewById(R.id.server_button);
		statusText = findViewById(R.id.status_text);
		addressText = findViewById(R.id.address_text);
		preferences = getSharedPreferences("server_preferences", MODE_PRIVATE);
		portInput.setText(String.valueOf(preferences.getInt("port", 8080)));
		findViewById(R.id.menu_button).setOnClickListener(view -> showMenu(view));
		serverButton.setOnClickListener(view -> toggleServer());
	}

	private void showMenu(View anchor) {
		PopupMenu menu = new PopupMenu(this, anchor);
		menu.getMenu().add("Konfigurasi server").setOnMenuItemClickListener(item -> {
			showConfigurationDialog();
			return true;
		});
		menu.getMenu().add(server != null && server.isRunning() ? "Hentikan server" : "Mulai server")
				.setOnMenuItemClickListener(item -> {
					toggleServer();
					return true;
				});
		menu.getMenu().add("Informasi alamat").setOnMenuItemClickListener(item -> {
				if (addressText.getText().length() == 0) {
					Toast.makeText(this, "Server belum aktif", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, addressText.getText(), Toast.LENGTH_LONG).show();
				}
				return true;
		});
		menu.show();
	}

	private void showConfigurationDialog() {
		LinearLayout form = new LinearLayout(this);
		form.setOrientation(LinearLayout.VERTICAL);
		int padding = (int) (20 * getResources().getDisplayMetrics().density);
		form.setPadding(padding, 0, padding, 0);

		EditText port = new EditText(this);
		port.setHint("Port HTTP");
		port.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
		port.setText(portInput.getText().toString());
		form.addView(port);

		EditText title = new EditText(this);
		title.setHint("Nama server");
		title.setText(preferences.getString("title", "hosts"));
		form.addView(title);

		new android.app.AlertDialog.Builder(this)
				.setTitle("Konfigurasi server")
				.setView(form)
				.setNegativeButton("Batal", null)
				.setPositiveButton("Simpan", (dialog, which) -> saveConfiguration(port, title))
				.show();
	}

	private void saveConfiguration(EditText portField, EditText titleField) {
		try {
			int port = addPort.parse(portField.getText().toString());
			String title = titleField.getText().toString().trim();
			if (title.isEmpty()) throw new IllegalArgumentException("Nama server wajib diisi");
			if (server != null && server.isRunning()) {
				Toast.makeText(this, "Hentikan server sebelum mengubah konfigurasi", Toast.LENGTH_LONG).show();
				return;
			}
			portInput.setText(String.valueOf(port));
			preferences.edit().putInt("port", port).putString("title", title).apply();
			Toast.makeText(this, "Konfigurasi tersimpan", Toast.LENGTH_SHORT).show();
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
		}
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

		String title = preferences.getString("title", "hosts");
		preferences.edit().putInt("port", port).apply();
		server = new localServer(port, title, new localServer.Listener() {
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
