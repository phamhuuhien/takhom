package com.example.takhom;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

	private EditText editText;
	public DatePickerFragment(EditText editText) {
		this.editText = editText;
	}
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
	
	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		editText.setText(arg3+"/"+(arg2+1) + "/" + arg1);
	}

}
