package com.folleach.donationalerts;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.folleach.daintegrate.DataCollector;
import org.json.JSONObject;

public class Donation
{
	public int ID;
	public String AlertType;
	public String IsShown;
	public AdditionalData AddData;
	public String BillingSystem;
	public String BillingSystemType;
	public String UserName;
	public float Amount;
	public String AmountFormatted;
	public float AmountMain;
	public String Currency;
	public String Message;
	public Date DateCreated;
	public String Emotes;
	public String ApId;
	public boolean IsTest;
	
	public static Donation getDonation(String data)
	{
		Donation obj = new Donation();
		JSONObject json;
		boolean mainDone = false;
		try {
			json = new JSONObject(data);
			//Main
			obj.ID = json.getInt("id");
			obj.Amount = (float) json.getDouble("amount");
			obj.AmountFormatted = json.getString("amount_formatted");
			obj.AmountMain = (float)json.getDouble("amount_main");
			obj.Currency = json.getString("currency");
			obj.UserName = json.getString("username");
			obj.Message = json.getString("message");
			obj.IsTest = json.getBoolean("_is_test_alert");
			mainDone = true;
			//Secondary
			obj.AlertType = json.getString("alert_type");
			obj.IsShown = json.getString("is_shown");
			obj.BillingSystem = json.getString("billing_system");
			obj.BillingSystemType = json.getString("billing_system_type");
			obj.DateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("date_created"));
			obj.Emotes = json.getString("emotes");
			obj.ApId = json.getString("ap_id");
			obj.AddData = AdditionalData.getAdditionalData(json.getString("additional_data"));
		} catch (Exception e) {
			if (mainDone)
				return obj;
			return null;
		}
		
		return obj;
	}
}