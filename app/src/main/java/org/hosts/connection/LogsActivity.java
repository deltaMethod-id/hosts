package org.hosts.connection;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LogsActivity extends BaseActivity {
	private ListView logList;
	private TextView emptyText;
	private ArrayAdapter<String> adapter;
	private java.util.ArrayList<String> logs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logList = findViewById(R.id.log_list);
		emptyText = findViewById(R.id.logs_empty);
		Button clearButton = findViewById(R.id.logs_clear);

		logs = new java.util.ArrayList<>(ServerInstance.getLogs());
		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, logs);
		logList.setAdapter(adapter);

		if (logs.isEmpty()) {
			emptyText.setVisibility(android.view.View.VISIBLE);
			logList.setVisibility(android.view.View.GONE);
		} else {
			emptyText.setVisibility(android.view.View.GONE);
			logList.setVisibility(android.view.View.VISIBLE);
		}

		clearButton.setOnClickListener(v -> {
			ServerInstance.clearLogs();
			logs.clear();
			adapter.notifyDataSetChanged();
			emptyText.setVisibility(android.view.View.VISIBLE);
			logList.setVisibility(android.view.View.GONE);
			Toast.makeText(this, "Log dihapus", Toast.LENGTH_SHORT).show();
		});
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_logs;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.logs_title);
	}

	@Override
	protected void onResume() {
		super.onResume();
		logs.clear();
		logs.addAll(ServerInstance.getLogs());
		adapter.notifyDataSetChanged();
		if (logs.isEmpty()) {
			emptyText.setVisibility(android.view.View.VISIBLE);
			logList.setVisibility(android.view.View.GONE);
		} else {
			emptyText.setVisibility(android.view.View.GONE);
			logList.setVisibility(android.view.View.VISIBLE);
		}
	}
}
