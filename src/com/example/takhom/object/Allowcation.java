package com.example.takhom.object;

import org.json.JSONException;
import org.json.JSONObject;

public class Allowcation {

	public String allowcation_id;
	public Double balance;
	public Double amount;
	public String expense;
	public String scenario_id;
	
	public Allowcation(JSONObject allowcation) {
		try {
			allowcation_id = allowcation.getString("pk");
			JSONObject fields = allowcation.getJSONObject("fields");
			balance = fields.getDouble("balance");
			amount = fields.getDouble("amount");
			expense = fields.getString("expense");
			scenario_id = fields.getString("scenario");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
