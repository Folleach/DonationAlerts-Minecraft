package com.folleach.donationalerts;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

@OnlyIn(Dist.CLIENT)
public class DonationType {
	public boolean Active;
	
	private List<String> Messages;
	private List<String> Commands;
	
	public String Name = "";
	public float CurrencyBRL;
	public float CurrencyBYN;
	public float CurrencyEUR;
	public float CurrencyKZT;
	public float CurrencyRUB;
	public float CurrencyUAH;
	public float CurrencyUSD;
	
	public float getAmountByCurrency(String currency) {
		switch (currency) {
		case "BRL": return CurrencyBRL; 
		case "BYN": return CurrencyBYN;
		case "EUR": return CurrencyEUR;
		case "KZT": return CurrencyKZT;
		case "RUB": return CurrencyRUB;
		case "UAH": return CurrencyUAH;
		case "USD": return CurrencyUSD;
		}
		return Float.POSITIVE_INFINITY;
	}
	
	public static DonationType getDonationType(String data) throws JSONException
	{
		JSONObject json = new JSONObject(data);
		DonationType typeObject = new DonationType();
		if (json.has("active"))
			typeObject.Active = json.getBoolean("active");
		if (json.has("messages"))
		{
			JSONArray messagesJsonArray = json.getJSONArray("messages");
			if (messagesJsonArray != null)
			{
				typeObject.Messages = new ArrayList<String>();
				for (int i = 0; i < messagesJsonArray.length(); i++)
					typeObject.Messages.add(messagesJsonArray.getString(i));
			}
		}
		if (json.has("commands"))
		{
			JSONArray commandJsonArray = json.getJSONArray("commands");
			if (commandJsonArray != null)
			{
				typeObject.Commands = new ArrayList<String>();
				for (int i = 0; i < commandJsonArray.length(); i++)
					typeObject.Commands.add(commandJsonArray.getString(i));
			}
		}
		if (json.has("BRL"))
			typeObject.CurrencyBRL = (float) json.getDouble("BRL");
		if (json.has("BYN"))
			typeObject.CurrencyBYN = (float) json.getDouble("BYN");
		if (json.has("EUR"))
			typeObject.CurrencyEUR = (float) json.getDouble("EUR");
		if (json.has("KZT"))
			typeObject.CurrencyKZT = (float) json.getDouble("KZT");
		if (json.has("RUB"))
			typeObject.CurrencyRUB = (float) json.getDouble("RUB");
		if (json.has("UAH"))
			typeObject.CurrencyUAH = (float) json.getDouble("UAH");
		if (json.has("USD"))
			typeObject.CurrencyUSD = (float) json.getDouble("USD");
		if (json.has("name"))
			typeObject.Name = json.getString("name");
		return typeObject;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try {
			json.put("active", Active);
			json.put("messages", Messages);
			json.put("commands", Commands);
			json.put("BRL", CurrencyBRL);
			json.put("BYN", CurrencyBYN);
			json.put("EUR", CurrencyEUR);
			json.put("KZT", CurrencyKZT);
			json.put("RUB", CurrencyRUB);
			json.put("UAH", CurrencyUAH);
			json.put("USD", CurrencyUSD);
			json.put("name", Name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public List<String> getMessages() {
		return Messages;
	}
	public List<String> getCommands() {
		return Commands;
	}
	public void AddMessage(String value) {
		if (Messages == null)
			Messages = Lists.<String>newArrayList();
		Messages.add(value);
	}
	public void AddCommand(String value) {
		if (Commands == null)
			Commands = Lists.<String>newArrayList();
		Commands.add(value);
	}
	public void setMessages(List<String> value) {
		Messages = value;
	}
	public void setCommands(List<String> value) {
		Commands = value;
	}
}
