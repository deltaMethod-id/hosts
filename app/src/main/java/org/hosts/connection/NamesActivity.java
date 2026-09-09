package org.hosts.connection;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NamesActivity extends BaseActivity {
	private EditText nameInput;
	private PreferencesManager prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);
		nameInput = findViewById(R.id.server_name);
		Button saveButton = findViewById(R.id.name_save);

		nameInput.setText(prefs.getTitle());
		saveButton.setOnClickListener(v -> saveName());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_name;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.names_title);
	}

	private void saveName() {
		String name = nameInput.getText().toString().trim();
		if (name.isEmpty()) {
			Toast.makeText(this, getString(R.string.toast_title_required), Toast.LENGTH_SHORT).show();
			return;
		}
		prefs.setTitle(name);
		Toast.makeText(this, getString(R.string.names_save) + ": " + name, Toast.LENGTH_SHORT).show();
		finish();
	}
}
