package org.hosts.connection;

import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView appName = findViewById(R.id.about_app_name);
		TextView version = findViewById(R.id.about_version);
		TextView description = findViewById(R.id.about_description);

		appName.setText(getString(R.string.about_app_name));
		version.setText(getString(R.string.about_version));
		description.setText(getString(R.string.about_description));
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_about;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.about_title);
	}
}
