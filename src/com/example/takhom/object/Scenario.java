package com.example.takhom.object;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Scenario {

	public String scenario_id;
	public Date paydate;
	public Double amount;
	public String mode;
	
	public Scenario(JSONObject scenario) {
		try {
			scenario_id = scenario.getString("pk");
			JSONObject fields = scenario.getJSONObject("fields");
			paydate = parseToDate(fields.getString("paycheck_date"));
			amount = fields.getDouble("paycheck_amount");
			mode = fields.getString("mode");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Date parseToDate(String date) {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd"); 
		try {
			return dt.parse(date);
		} catch (ParseException e) {
			return null;
		} 
	}
}
