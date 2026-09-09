package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.hosts.connection.config.ip;

public class IpActivity extends BaseActivity {
	private EditText ipInput;
	private PreferencesManager prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);
		ipInput = findViewById(R.id.ip_value);
		Button saveButton = findViewById(R.id.ip_save);

		ipInput.setText(prefs.getIp());
		saveButton.setOnClickListener(v -> saveIp());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_ip;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.ip_title);
	}

	private void saveIp() {
		try {
			String ipValue = ipInput.getText().toString();
			if (!ip.isValid(ipValue)) {
				throw new IllegalArgumentException(getString(R.string.toast_invalid_ip));
			}
			prefs.setIp(ipValue);
			Toast.makeText(this, getString(R.string.ip_save) + ": " + ipValue, Toast.LENGTH_SHORT).show();
			finish();
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
