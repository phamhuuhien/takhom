package com.example.takhom;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class LoginFragment extends SherlockFragment {

	private static Handler mHandler = new Handler();
	private ProgressDialog progressDialog;
	private EditText email, pin;
	private TextView forgot_password_text;
	private TextView register_account_text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.login_fragment, container, false);

		Button login_button = (Button)rootView.findViewById(R.id.login_button);
		email = (EditText)rootView.findViewById(R.id.email);
		email.setText("tranvanthien113@gmail.com");
		
		forgot_password_text = (TextView)rootView.findViewById(R.id.forgot_password_text);
		register_account_text = (TextView)rootView.findViewById(R.id.register_account_text);
		pin = (EditText)rootView.findViewById(R.id.pin);

		login_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				progressDialog = ProgressDialog.show(getActivity(), "", "Login ...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						login(email.getText().toString(), pin.getText().toString());
					}}).start();
			}
		});
		
		forgot_password_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forgotPassword();
			}
		});
		
		register_account_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent it = new Intent(getActivity(), RegisterActivity.class );
				startActivity(it);
			}
		});
		
		return rootView;
	}

	public void login(final String username, final String password) {
		HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 10000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 10000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient client = new DefaultHttpClient(httpParameters);  
		HttpPost post = new HttpPost("https://www.takehome.us/develop/login/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("email", username);
			obj.put("password", password);

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			// CONVERT RESPONSE TO STRING
			final String result = EntityUtils.toString(response.getEntity());
			System.out.println("result="+result);
			final JSONObject jsonObj = new JSONObject(result);
			final int code = jsonObj.getInt("error_code");
			final String message = jsonObj.getString("message");
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					progressDialog.dismiss();
					if(code == 0) {
						try {
							final JSONObject jsonObj = new JSONObject(message);
							final String token = jsonObj.getString("token");
							System.out.println("token="+token);
							SharedPreferences mPrefs = getActivity().getSharedPreferences("LSprefs",0);
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("TOKEN", token);
							editor.commit();
							MainActivity.isLogin = true;

							((MainActivity)getActivity()).click("Home");
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						try {
							AlertDialog alertDialog = new AlertDialog.Builder(
									getActivity()).create();
							alertDialog.setTitle("Error: " +code);
							alertDialog.setMessage(message);

							// Setting Icon to Dialog
							alertDialog.setIcon(R.drawable.ic_launcher);
							alertDialog.show();
						} catch (Exception e) {

						}
					}
				}});

		} catch (Exception e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					progressDialog.dismiss();
				}});
		}
	}
	
	public void forgotPassword() {
		LayoutInflater li = LayoutInflater.from(getActivity());
		View myView = li.inflate(R.layout.forgot_password, null);

		AlertDialog.Builder cDialog = new AlertDialog.Builder(getActivity());
		cDialog.setTitle("Reset your password");
		cDialog.setIcon(R.drawable.ic_launcher);
		cDialog.setView(myView);
		//AlertDialog tmp = cDialog.create();
		final EditText email = (EditText) myView.findViewById(R.id.email);
		email.setText("tranvanthien113@gmail.com");
		cDialog.setPositiveButton("Ok", new   DialogInterface.OnClickListener() {
			  @Override
			  public void onClick(DialogInterface dialog, int which) {
				  dialog.cancel();
				  //progressDialog = ProgressDialog.show(getActivity(), "", "Sending mail ...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							forgotpass(email.getText().toString());
						}}).start();
			  }
			});
			cDialog.setNegativeButton("Cancel", new   DialogInterface.OnClickListener() {
				  @Override
				  public void onClick(DialogInterface dialog, int which) {
				    //here the code to retrieve dialog
				  }
				});
		cDialog.create();
		cDialog.show();
	}
	
	public void forgotpass(final String username) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost("https://www.takehome.us/develop/resetrequest/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("email", username);

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post);
			// CONVERT RESPONSE TO STRING
			final String result = EntityUtils.toString(response.getEntity());
			final JSONObject jsonObj = new JSONObject(result);
			final int code = jsonObj.getInt("error_code");
			final String message = jsonObj.getString("message");
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					//progressDialog.dismiss();
					
					AlertDialog alertDialog = new AlertDialog.Builder(
							getActivity()).create();
					alertDialog.setTitle("Code:" +code);
					alertDialog.setMessage(message);

					// Setting Icon to Dialog
					alertDialog.setIcon(R.drawable.ic_launcher);
					alertDialog.show();

				}});

		} catch (Exception e) {
			e.printStackTrace();
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					//progressDialog.dismiss();
				}});
		}
	}

}
