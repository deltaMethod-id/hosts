package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hosts.connection.tools.addPort;

public class ConfigActivity extends BaseActivity {
	private EditText portInput;
	private EditText titleInput;
	private PreferencesManager prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);
		portInput = findViewById(R.id.config_port);
		titleInput = findViewById(R.id.config_title);
		Button saveButton = findViewById(R.id.config_save);

		portInput.setText(String.valueOf(prefs.getPort()));
		titleInput.setText(prefs.getTitle());
		saveButton.setOnClickListener(v -> saveConfig());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_config;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.config_title);
	}

	private void saveConfig() {
		try {
			int port = addPort.parse(portInput.getText().toString());
			String title = titleInput.getText().toString().trim();
			if (title.isEmpty()) throw new IllegalArgumentException(getString(R.string.toast_title_required));
			if (ServerInstance.getServer() != null && ServerInstance.getServer().isRunning()) {
				Toast.makeText(this, getString(R.string.toast_stop_first), Toast.LENGTH_LONG).show();
				return;
			}
			prefs.setPort(port);
			prefs.setTitle(title);
			Toast.makeText(this, getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
			finish();
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
