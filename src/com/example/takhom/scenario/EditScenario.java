package com.example.takhom.scenario;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.takhom.DatePickerFragment;
import com.example.takhom.R;

public class EditScenario extends SherlockFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.scenario_edit, container, false);
		return rootView;
	}
}
