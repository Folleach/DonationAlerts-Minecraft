package com.folleach.donationalerts;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

public class DonationAlertsEvent
{
	public int ID;
	public AlertType Type;
	public String IsShown;
	public AdditionalData Additional;
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
	
	public static DonationAlertsEvent getDonationAlertsEvent(String data)
	{
		DonationAlertsEvent obj = new DonationAlertsEvent();
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
			if (json.has("alert_type"))
				obj.Type = AlertType.valueOf(json.getInt("alert_type"));
			else
				obj.Type = AlertType.Undefined;
			mainDone = true;
			//Secondary
			obj.IsShown = json.getString("is_shown");
			obj.BillingSystem = json.getString("billing_system");
			obj.BillingSystemType = json.getString("billing_system_type");
			obj.DateCreated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(json.getString("date_created"));
			obj.Emotes = json.getString("emotes");
			obj.ApId = json.getString("ap_id");
			obj.Additional = AdditionalData.getAdditionalData(json.getString("additional_data"));
		} catch (Exception e) {
			if (mainDone)
				return obj;
			return null;
		}
		
		return obj;
	}
}