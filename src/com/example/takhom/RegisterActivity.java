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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;

public class RegisterActivity extends SherlockActivity {
	private static Handler mHandler = new Handler();
	private ProgressDialog progressDialog;
	private EditText email, pin;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(MainActivity.THEME); //Used for theme switching in samples
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);	
		Button register_button = (Button)findViewById(R.id.register_button);
		register_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Thread(new Runnable() {
					@Override
					public void run() {
						register(email.getText().toString(), pin.getText().toString());
					}}).start();
			}
		});
		}
	//}
	public void register(final String username, final String password) {
		HttpParams httpParameters = new BasicHttpParams();
	    int timeoutConnection = 10000;
	    HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	    int timeoutSocket = 10000;
	    HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		HttpClient client = new DefaultHttpClient(httpParameters);  
		HttpPost post = new HttpPost("https://www.takehome.us/develop/register/");//url ne 
		post.setHeader("Content-type", "application/json"); // kieu du lieu thoi

		try {
			JSONObject obj = new JSONObject();
			obj.put("email", username);
			obj.put("password", password); // dua du lieu len API

			post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			HttpResponse response = client.execute(post); // lay du lieu ve ne
			// CONVERT RESPONSE TO STRING
			final String result = EntityUtils.toString(response.getEntity());
			System.out.println("result="+result); // in ra ket qua tra ve day
			final JSONObject jsonObj = new JSONObject(result); // vi no la json object nen phai lam vay de lay du lieu
			final int code = jsonObj.getInt("error_code"); // lay field code
			final String message = jsonObj.getString("message"); // lay message
			mHandler.post(new Runnable() { // thang mHandler chua dc khai bao
				@Override
				public void run() {
					progressDialog.dismiss(); // progressDialog chua dc khai bao
					if(code == 0) { // code =0 la success ne
						System.out.println("You have successfully registered. Please check mail to complete.");
					} else {
						System.out.println("Invalid email or password.");
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
	
}
