package org.hosts.connection;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hosts.connection.config.ip;

public class IpActivity extends Activity {
	private EditText ipInput;
	private PreferencesManager prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip);

		prefs = new PreferencesManager(this);
		ipInput = findViewById(R.id.ip_value);
		Button saveButton = findViewById(R.id.ip_save);

		ipInput.setText(prefs.getIp());
		saveButton.setOnClickListener(v -> saveIp());
	}

	private void saveIp() {
		try {
			String ipValue = ipInput.getText().toString();
			if (!ip.isValid(ipValue)) {
				throw new IllegalArgumentException("Format IP tidak valid (gunakan xxx.xxx.xxx.xxx)");
			}
			prefs.setIp(ipValue);
			Toast.makeText(this, "IP tersimpan: " + ipValue, Toast.LENGTH_SHORT).show();
			finish();
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
