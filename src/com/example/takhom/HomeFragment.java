package com.example.takhom;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.takhom.scenario.ScenarioActivity;
import com.example.takhom.widgets.ListObject;
import com.example.takhom.widgets.MyArrayAdapter;

public class HomeFragment extends SherlockFragment {

	public static String ACTIVE_SCENARIO = "activeScenario";
	private LinearLayout chart;
	private CategorySeries mSeries = new CategorySeries("");  
	private DefaultRenderer mRenderer = new DefaultRenderer(); 

	private static final Handler mHandler = new Handler();
	private ListView listview;
	private ProgressDialog progressDialog;

	private FrameLayout takehome_layout, paycheck_layout;
	private TextView paycheck_number, takehome_number;

	private JSONObject activeScenario = null;

	private GraphicalView mChartView;  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);

		progressDialog = ProgressDialog.show(getActivity(), "", "Loading catalogies ...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				SharedPreferences mPrefs = getActivity().getSharedPreferences("LSprefs",0);
				String token = mPrefs.getString("TOKEN", null);
				getCatalogies(token);
				getListScenario(token);
			}}).start();

		listview = (ListView)rootView.findViewById(R.id.listview);
		chart = (LinearLayout) rootView.findViewById(R.id.chart);

		takehome_layout = (FrameLayout) rootView.findViewById(R.id.takehome_layout);
		paycheck_layout = (FrameLayout) rootView.findViewById(R.id.paycheck_layout);

		takehome_number = (TextView) rootView.findViewById(R.id.takehome_number);
		paycheck_number = (TextView) rootView.findViewById(R.id.paycheck_number);

		takehome_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openScenario();
			}
		});

		paycheck_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openScenario();
			}
		});


		mRenderer.setApplyBackgroundColor(true);  
		mRenderer.setBackgroundColor(Color.argb(0, 255, 255, 255));
		//mRenderer.setChartTitle("");
		mRenderer.setChartTitleTextSize(20);  
		mRenderer.setLabelsTextSize(15);  
		mRenderer.setLegendTextSize(15);  
		mRenderer.setMargins(new int[] { 20, 30, 15, 0 });  
		mRenderer.setZoomButtonsVisible(true);  
		mRenderer.setStartAngle(90);  
		mRenderer.setShowLegend(false);


		return rootView;
	}

	private void setChartInfo(List<ListObject> list) {
		MyArrayAdapter adapter = new MyArrayAdapter(getSherlockActivity(), list);
		listview.setAdapter(adapter);
		listview.setVisibility(View.VISIBLE);

		for (int i = 0; i < list.size() ; i++) {  
			mSeries.add(list.get(i).text + " " + list.get(i).number, Double.parseDouble(list.get(i).number));  
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();  
			renderer.setColor(list.get(i).color);  
			mRenderer.addSeriesRenderer(renderer);  
		}  

		if (mChartView != null) {  
			mChartView.repaint();  
		}  
	}

	@Override
	public void onResume() {  
		super.onResume();  
		if (mChartView == null) {  
			//LinearLayout layout = (LinearLayout) findViewById(R.id.chart);  
			mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);  
			mRenderer.setClickEnabled(true);  
			mRenderer.setSelectableBuffer(10);  

			mChartView.setOnClickListener(new View.OnClickListener() {  
				@Override  
				public void onClick(View v) {  
					//					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();  
					//
					//					if (seriesSelection == null) {  
					//						Toast.makeText(getActivity(),"No chart element was clicked",Toast.LENGTH_SHORT).show();  
					//					} else {  
					//						Toast.makeText(getActivity(),"Chart element data point index "+ (seriesSelection.getPointIndex()+1) + " was clicked" + " point value="+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();  
					//					}  
				}  
			});  

			mChartView.setOnLongClickListener(new View.OnLongClickListener() {  
				@Override  
				public boolean onLongClick(View v) {  
					//					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();  
					//					if (seriesSelection == null) {  
					//						Toast.makeText(getActivity(),"No chart element was long pressed", Toast.LENGTH_SHORT);  
					//						return false;   
					//					} else {  
					//						Toast.makeText(getActivity(),"Chart element data point index "+ seriesSelection.getPointIndex()+ " was long pressed",Toast.LENGTH_SHORT);  
					//						return true;         
					//					}  
					return true;
				}  
			});  
			chart.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
		}  
		else {  
			mChartView.repaint();  
		}  
	}  


	public void getCatalogies(final String token) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost(MainActivity.BASE_URL + "category/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("method", "get");

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
					if(code == 0) {
						setChartInfo(parseJson(message));
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								progressDialog.dismiss();
							}});
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

	private List<ListObject> parseJson(final String json) {
		List<ListObject> list = new ArrayList<ListObject>();
		try {
			JSONArray catalogies = new JSONArray(json);
			for(int i=0; i< catalogies.length(); i++) {
				JSONObject tmp = catalogies.getJSONObject(i);
				String text = tmp.getString("fields");
				JSONObject item = new JSONObject(text);

				ListObject object = new ListObject();
				String catalogy = item.getString("name");
				if(catalogy.equals("Loans")) {
					object.imageId = R.drawable.loans;
				} else if (catalogy.equals("Credit Cards")) {
					object.imageId = R.drawable.creditcards;
				} else if (catalogy.equals("Utilities")) {
					object.imageId = R.drawable.utilities;
				} else if (catalogy.equals("Other Debts")) {
					object.imageId = R.drawable.otherdebts;
				}
				object.text = catalogy.toUpperCase();
				object.number = "100";
				object.color = Color.parseColor(item.getString("color"));

				list.add(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private void openScenario() {
		Intent it = new Intent(getActivity(), ScenarioActivity.class );
		it.putExtra(ACTIVE_SCENARIO, activeScenario==null?null:activeScenario.toString());
		startActivity(it);
	}

	public void getListScenario(String token) {
		HttpClient client = new DefaultHttpClient();  
		HttpPost post = new HttpPost(MainActivity.BASE_URL + "scenario/"); 
		post.setHeader("Content-type", "application/json");

		try {
			JSONObject obj = new JSONObject();
			obj.put("token", token);
			obj.put("method", "get");

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
					if(code == 0) {
						try {
							JSONArray scenarios = new JSONArray(message);
							//System.out.println("message="+message);
							for(int i=0; i< scenarios.length(); i++) {
								JSONObject scenario = new JSONObject(scenarios.getJSONObject(i).getString("fields"));
								if(scenario.getString("mode").equals("P") || scenario.getString("mode").equals("A")) {
									takehome_number.setText(scenario.getString("paycheck_amount"));
									paycheck_number.setText(scenario.getString("paycheck_amount"));
									activeScenario = scenarios.getJSONObject(i);
								}
							}
						} catch (Exception e) {
							progressDialog.dismiss();
						}

						mHandler.post(new Runnable() {
							@Override
							public void run() {
								progressDialog.dismiss();
							}});
					}

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

