package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hosts.connection.tools.addPort;

public class PortActivity extends BaseActivity {
	private EditText portInput;
	private PreferencesManager prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);
		portInput = findViewById(R.id.port_input);
		Button saveButton = findViewById(R.id.port_save);

		portInput.setText(String.valueOf(prefs.getPort()));
		saveButton.setOnClickListener(v -> savePort());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_port;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.port_title);
	}

	private void savePort() {
		try {
			int port = addPort.parse(portInput.getText().toString());
			prefs.setPort(port);
			Toast.makeText(this, getString(R.string.port_save) + ": " + port, Toast.LENGTH_SHORT).show();
			finish();
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
