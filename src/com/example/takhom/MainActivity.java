package com.example.takhom;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MainActivity extends SherlockFragmentActivity {
	private TextView mSelected;
	private String[] mLocations;
	public static int THEME = R.style.Theme_Sherlock_Light;
	public LoginFragment loginFragment = null;
	public TestFragment testFragment = null;

	public static boolean isLogin = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(THEME); //Used for theme switching in samples
		super.onCreate(savedInstanceState);

		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(loginFragment == null)
			loginFragment = new LoginFragment();
		ft.replace(android.R.id.content, loginFragment);
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		click(item.getTitle().toString());
		return true;
	}

	public void click(String title) {
		if(!isLogin) return;
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(title.equals("Home")) {
			ft.replace(android.R.id.content, new HomeFragment());
		} else if (title.equals("Archives")) {
			ft.replace(android.R.id.content, new TestFragment());
		} else if (title.equals("Help")) {
			//ft.replace(android.R.id.content, new HomeFragment());
		}
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu subMenu1 = menu.addSubMenu("Action Item");
		subMenu1.add("Home");
		subMenu1.add("Archives");
		subMenu1.add("Help");

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_light);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add("Save")
		.setIcon(R.drawable.img_girl)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}
}
