package com.example.takhom;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;

public class MainActivity extends SherlockFragmentActivity {
	public static int THEME = R.style.Theme_Sherlock_Light;
	public static String BASE_URL = "https://takehome.us/develop/";
	public static String email = "Email";
	public static String HOME = "Home";
	public static String ARCHIVES = "Archives";
	public static String LOGOUT = "Logout";
	public static String LOGIN = "Login";
	public static String HELP = "Help";

	protected Menu mMenu = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(THEME); //Used for theme switching in samples
		super.onCreate(savedInstanceState);


		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(getToken() != null) {
			ft.replace(android.R.id.content, new HomeFragment(), HOME);
		} else {
			ft.replace(android.R.id.content, new LoginFragment(), LOGIN);
		}
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		click(item.getTitle().toString());
		return true;
	}

	public void click(String title) {
		updateMenu();
		if(getToken() == null) return;
		Fragment item = getSupportFragmentManager().findFragmentByTag(title);
		if(item != null && item.isAdded()) return; // fragment already exist
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(title.equals(HOME)) {
			ft.replace(android.R.id.content, new HomeFragment(), HOME);
		} else if (title.equals(ARCHIVES)) {
			ft.replace(android.R.id.content, new TestFragment(), ARCHIVES);
		} else if (title.equals(HELP)) {

		} else if (title.equals(LOGOUT)) {
			SharedPreferences mPrefs = getSharedPreferences("LSprefs",0);
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.remove("TOKEN");
			editor.commit();
			ft.replace(android.R.id.content, new LoginFragment(), LOGIN);
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		SubMenu subMenu1 = menu.addSubMenu("Action Item");
		subMenu1.add(email);
		subMenu1.add("Home");
		subMenu1.add("Archives");
		subMenu1.add("Profile");
		subMenu1.add("Help");
		subMenu1.add("Logout");

		MenuItem subMenu1Item = subMenu1.getItem();
		subMenu1Item.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_light);
		subMenu1Item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menu.add("Save")
		.setIcon(R.drawable.img_girl)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		mMenu = menu;
		return super.onCreateOptionsMenu(menu);
	}

	public void updateMenu() {
		Fragment item = getSupportFragmentManager().findFragmentByTag(LOGIN);

		if(item instanceof LoginFragment) {
			SubMenu tmp = (SubMenu)mMenu.getItem(0).getSubMenu();
			tmp.getItem(0).setVisible(false);
			tmp.getItem(5).setVisible(false);
		} else {
			SubMenu tmp = (SubMenu)mMenu.getItem(0).getSubMenu();
			tmp.getItem(0).setVisible(true);
			tmp.getItem(0).setTitle(email);
			tmp.getItem(5).setVisible(true);
		}
	}

	public String getToken() {
		SharedPreferences mPrefs = getSharedPreferences("LSprefs",0);
		return mPrefs.getString("TOKEN", null);
	}
}
