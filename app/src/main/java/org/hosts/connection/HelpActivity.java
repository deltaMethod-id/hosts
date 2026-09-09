package org.hosts.connection;

import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView help1 = findViewById(R.id.help_step1);
		TextView help2 = findViewById(R.id.help_step2);
		TextView help3 = findViewById(R.id.help_step3);
		TextView help4 = findViewById(R.id.help_step4);
		TextView help5 = findViewById(R.id.help_step5);

		help1.setText(getString(R.string.help_step1));
		help2.setText(getString(R.string.help_step2));
		help3.setText(getString(R.string.help_step3));
		help4.setText(getString(R.string.help_step4));
		help5.setText(getString(R.string.help_step5));
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_help;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.help_title);
	}
}
