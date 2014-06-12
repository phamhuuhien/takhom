package com.example.takhom;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.takhom.widgets.ListObject;
import com.example.takhom.widgets.MyArrayAdapter;

public class HomeFragment extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		
		ListView listview = (ListView)rootView.findViewById(R.id.listview);
		List<ListObject> list = new ArrayList<ListObject>();
		ListObject object = new ListObject();
		object.imageId = R.drawable.creditcards;
		object.text = "CREDIT CARDS";
		object.number = "100";
		object.color = Color.parseColor("#FA6E56");
		
		list.add(object);
		
		object = new ListObject();
		object.imageId = R.drawable.loans;
		object.text = "LOANS";
		object.number = "150";
		object.color = Color.parseColor("#566375");
		list.add(object);
		
		object = new ListObject();
		object.imageId = R.drawable.utilities;
		object.text = "UTILITIES";
		object.number = "100";
		object.color = Color.parseColor("#F6AE31");
		list.add(object);
		
		object = new ListObject();
		object.imageId = R.drawable.otherdebts;
		object.text = "OTHER DEBTS";
		object.number = "800";
		object.color = Color.parseColor("#4865AE");
		list.add(object);
		
		MyArrayAdapter adapter = new MyArrayAdapter(getSherlockActivity(), list);
		listview.setAdapter(adapter);
		listview.setVisibility(View.VISIBLE);
		
		return rootView;
	}
}

