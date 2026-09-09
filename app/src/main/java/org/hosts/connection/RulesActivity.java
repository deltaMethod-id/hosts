package org.hosts.connection;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RulesActivity extends BaseActivity {
	private EditText ruleInput;
	private ListView ruleList;
	private PreferencesManager prefs;
	private ArrayAdapter<String> adapter;
	private List<String> rules;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = new PreferencesManager(this);
		ruleInput = findViewById(R.id.rule_input);
		ruleList = findViewById(R.id.rule_list);
		Button addButton = findViewById(R.id.rule_add);

		rules = new ArrayList<>();
		loadRules();

		adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rules);
		ruleList.setAdapter(adapter);

		ruleList.setOnItemLongClickListener((parent, view, position, id) -> {
			prefs.removeRule(position);
			loadRules();
			adapter.notifyDataSetChanged();
			Toast.makeText(this, getString(R.string.rules_deleted), Toast.LENGTH_SHORT).show();
			return true;
		});

		addButton.setOnClickListener(v -> addRule());
	}

	@Override
	protected int getContentLayoutId() {
		return R.layout.activity_rules;
	}

	@Override
	protected String getActivityTitle() {
		return getString(R.string.rules_title);
	}

	private void addRule() {
		String rule = ruleInput.getText().toString().trim();
		if (rule.isEmpty()) {
			Toast.makeText(this, getString(R.string.rules_required), Toast.LENGTH_SHORT).show();
			return;
		}
		prefs.addRule(rule);
		ruleInput.setText("");
		loadRules();
		adapter.notifyDataSetChanged();
		Toast.makeText(this, getString(R.string.rules_added), Toast.LENGTH_SHORT).show();
	}

	private void loadRules() {
		rules.clear();
		int count = prefs.getRuleCount();
		for (int i = 0; i < count; i++) {
			String rule = prefs.getRule(i);
			if (!rule.isEmpty()) {
				rules.add(rule);
			}
		}
	}
}
