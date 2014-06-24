package com.example.takhom;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;

public class EditScenario extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.scenario_new, container, false);
////		amount = (EditText)rootView.findViewById(R.id.amout);
////		payday = (EditText)rootView.findViewById(R.id.payday);
//
//		disableSoftInputFromAppearing(payday);
//
//		payday.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				DialogFragment newFragment = new DatePickerFragment(payday);
//				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
//			}
//		});


		return rootView;
	}
}
