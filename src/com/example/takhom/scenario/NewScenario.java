package com.example.takhom.scenario;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.takhom.DatePickerFragment;
import com.example.takhom.MainActivity;
import com.example.takhom.R;
import com.example.takhom.R.id;
import com.example.takhom.R.layout;

public class NewScenario extends SherlockFragment {

	private EditText amount, payday;
	private Button submit_button;

	private static Handler mHandler = new Handler();
	private ProgressDialog progressDialog;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.scenario_new, container, false);
		amount = (EditText)rootView.findViewById(R.id.amout);
		payday = (EditText)rootView.findViewById(R.id.payday);

		disableSoftInputFromAppearing(payday);

		payday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment(payday);
				newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
			}
		});

		submit_button = (Button)rootView.findViewById(R.id.submit_button);
		submit_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});


		return rootView;
	}

	/**
	 * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
	 * @param editText
	 */
	public static void disableSoftInputFromAppearing(EditText editText) {
		if (Build.VERSION.SDK_INT >= 11) {
			editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
			editText.setTextIsSelectable(true);
		} else {
			editText.setRawInputType(InputType.TYPE_NULL);
			editText.setFocusable(true);
		}
	}

	public void newScenario(final String token, final float amount, final String date) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost(MainActivity.BASE_URL +"scenario/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("method", "create");
			obj.put("paycheck_amount", amount);
			obj.put("paycheck_date", date);

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			// CONVERT RESPONSE TO STRING
			final String result = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObj = new JSONObject(result);
			final int code = jsonObj.getInt("error_code");
			final String message = jsonObj.getString("message");
			if(code == 0) {
				
			} else {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();
						AlertDialog.Builder cDialog = new AlertDialog.Builder(getActivity());
						cDialog.setTitle("Success");
						cDialog.setMessage("We've emailed you instructions for setting your PIN. You should be receiving them shortly.");
						cDialog.setPositiveButton("OK", null);
						cDialog.create();
						cDialog.show();
					}});
			}

		} catch (Exception e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					progressDialog.dismiss();
				}});
		}
	}
}
