package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DnsActivity extends BaseActivity {
	private EditText hostnameInput;
	private EditText addressInput;
	private PreferencesManager prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);
		hostnameInput = findViewById(R.id.dns_hostname);
		addressInput = findViewById(R.id.dns_address);
		Button saveButton = findViewById(R.id.dns_save);

		hostnameInput.setText(prefs.getDnsHostname());
		addressInput.setText(prefs.getDnsAddress());
		saveButton.setOnClickListener(v -> saveDns());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_dns;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.dns_title);
	}

	private void saveDns() {
		try {
			String hostname = hostnameInput.getText().toString().trim();
			String address = addressInput.getText().toString().trim();

			if (hostname.isEmpty()) throw new IllegalArgumentException(getString(R.string.dns_hostname) + " wajib diisi");
			if (address.isEmpty()) throw new IllegalArgumentException(getString(R.string.dns_address) + " wajib diisi");

			prefs.setDnsHostname(hostname);
			prefs.setDnsAddress(address);
			Toast.makeText(this, hostname + " -> " + address, Toast.LENGTH_SHORT).show();
			finish();
		} catch (IllegalArgumentException error) {
			Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
}
