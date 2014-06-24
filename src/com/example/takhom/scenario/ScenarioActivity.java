package com.example.takhom.scenario;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.takhom.HomeFragment;
import com.example.takhom.MainActivity;

public class ScenarioActivity extends SherlockFragmentActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(MainActivity.THEME); //Used for theme switching in samples
		super.onCreate(savedInstanceState);

		String jsonObject = getIntent().getStringExtra(HomeFragment.ACTIVE_SCENARIO);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(jsonObject == null) {
			ft.replace(android.R.id.content, new NewScenario(), "New");
		} else {
			ft.replace(android.R.id.content, new EditScenario(), "Edit");
		}
		ft.commit();

	}
}
