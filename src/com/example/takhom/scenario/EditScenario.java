package com.example.takhom.scenario;

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.takhom.MainActivity;
import com.example.takhom.R;
import com.example.takhom.object.Allowcation;
import com.example.takhom.object.Scenario;

public class EditScenario extends SherlockFragment {

	private static Handler mHandler = new Handler();
	private ProgressDialog progressDialog;
	private TableLayout allowcation_table;
	private TextView payday_countdown;

	private Scenario scenario;

	public EditScenario(String scenario) {
		try {
			System.out.println("scenario="+scenario);
			this.scenario = new Scenario(new JSONObject(scenario));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.scenario_edit, container, false);

		allowcation_table = (TableLayout)rootView.findViewById(R.id.allowcation_table);
		payday_countdown = (TextView)rootView.findViewById(R.id.payday_countdown);
//		System.out.println("scenario.paydate="+scenario.paydate.toString());
//		System.out.println("new Date()="+new Date().toString());
//		long days = daysBetween(scenario.paydate, new Date());
//		payday_countdown.setText(days + " days to payday");

		progressDialog = ProgressDialog.show(getActivity(), "", "Loading allowcations ...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences mPrefs = getActivity().getSharedPreferences("LSprefs",0);
				String token = mPrefs.getString("TOKEN", null);
				getAllowcation(token, scenario.scenario_id);
			}}).start();
		return rootView;
	}
	
	public long daysBetween(Date d1, Date d2){
        //return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		return Math.round((d2.getTime() - d1.getTime()) / (double) 86400000);
}

	public void getAllowcation(final String token, final String scenario_id) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost(MainActivity.BASE_URL +"allocation/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("method", "get");
			obj.put("scenario_id", scenario_id);

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			// CONVERT RESPONSE TO STRING
			final String result = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObj = new JSONObject(result);
			final int code = jsonObj.getInt("error_code");
			final String message = jsonObj.getString("message");
			if(code == 0) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();
						try {
							JSONArray allowcations = new JSONArray(message);
							for(int i=0; i< allowcations.length(); i++) {
								Allowcation allowcation = new Allowcation(allowcations.getJSONObject(i));
								LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								View myView = inflater.inflate(R.layout.table_row, null);
								TextView textView1 = (TextView)myView.findViewById(R.id.textView1);
								textView1.setText(allowcation.expense);
								TextView textView2 = (TextView)myView.findViewById(R.id.textView2);
								textView2.setText("catelogy");
								TextView textView3 = (TextView)myView.findViewById(R.id.textView3);
								textView3.setText(allowcation.amount.toString());
								TextView textView4 = (TextView)myView.findViewById(R.id.textView4);
								textView4.setText("123");
								allowcation_table.addView(myView, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}});
			} else {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						progressDialog.dismiss();
						//						AlertDialog.Builder cDialog = new AlertDialog.Builder(getActivity());
						//						cDialog.setTitle("Success");
						//						cDialog.setMessage("We've emailed you instructions for setting your PIN. You should be receiving them shortly.");
						//						cDialog.setPositiveButton("OK", null);
						//						cDialog.create();
						//						cDialog.show();
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
