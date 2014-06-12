package com.example.takhom;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
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
    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    	if(item.getTitle().equals("Home")) {
    		//if(loginFragment == null)
        		loginFragment = new LoginFragment();
        	ft.replace(android.R.id.content, loginFragment);
    	} else if (item.getTitle().equals("Archives")) {
    		//if(testFragment == null)
        		testFragment = new TestFragment();
        	ft.replace(android.R.id.content, testFragment);
    	} else if (item.getTitle().equals("Help")) {
    		ft.replace(android.R.id.content, new HomeFragment());
    	}
    	ft.commit();
        //Toast.makeText(this, "Got click: " + item.getTitle() , Toast.LENGTH_SHORT).show();
        return true;
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

//        SubMenu subMenu2 = menu.addSubMenu("Overflow Item");
//        subMenu2.add("These");
//        subMenu2.add("Are");
//        subMenu2.add("Sample");
//        subMenu2.add("Items");
//
//        MenuItem subMenu2Item = subMenu2.getItem();
//        subMenu2Item.setIcon(R.drawable.ic_compose);

        return super.onCreateOptionsMenu(menu);
    }
}
