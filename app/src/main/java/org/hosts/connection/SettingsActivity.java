package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends BaseActivity {
	private PreferencesManager prefs;
	private Button resetButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);

		findViewById(R.id.setting_dark_mode).setOnCheckedChangeListener((buttonView, isChecked) -> prefs.setDarkMode(isChecked));
		findViewById(R.id.setting_auto_start).setOnCheckedChangeListener((buttonView, isChecked) -> prefs.setAutoStart(isChecked));
		findViewById(R.id.setting_notification).setOnCheckedChangeListener((buttonView, isChecked) -> prefs.setNotificationEnabled(isChecked));

		resetButton = findViewById(R.id.setting_reset);
		resetButton.setOnClickListener(v -> {
			prefs.clearAll();
			Toast.makeText(this, "Data direset", Toast.LENGTH_SHORT).show();
		});
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_settings;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.settings_title);
	}
}
