package org.hosts.connection;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;
	private NavigationView navigationView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		drawerLayout = findViewById(R.id.drawer_layout);
		navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
				R.string.drawer_open,
				R.string.drawer_close);
		drawerLayout.addDrawerListener(drawerToggle);
		drawerToggle.syncState();

		FrameLayout contentFrame = findViewById(R.id.content_frame);
		View content = getLayoutInflater().inflate(getContentLayoutId(), contentFrame, false);
		contentFrame.addView(content);

		setTitle(getActivityTitle());
	}

	protected abstract int getContentLayoutId();

	protected abstract String getActivityTitle();

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.nav_dashboard) {
			navigateTo(DashboardActivity.class);
		} else if (id == R.id.nav_server) {
			navigateTo(MainActivity.class);
		} else if (id == R.id.nav_ip) {
			navigateTo(IpActivity.class);
		} else if (id == R.id.nav_dns) {
			navigateTo(DnsActivity.class);
		} else if (id == R.id.nav_rules) {
			navigateTo(RulesActivity.class);
		} else if (id == R.id.nav_logs) {
			navigateTo(LogsActivity.class);
		} else if (id == R.id.nav_settings) {
			navigateTo(SettingsActivity.class);
		} else if (id == R.id.nav_about) {
			navigateTo(AboutActivity.class);
		} else if (id == R.id.nav_help) {
			navigateTo(HelpActivity.class);
		}
		drawerLayout.closeDrawer(GravityCompat.START);
		return true;
	}

	private void navigateTo(Class<?> clazz) {
		if (clazz.equals(this.getClass())) {
			return;
		}
		Intent intent = new Intent(this, clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
}
