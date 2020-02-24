package com.folleach.donationalerts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
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
	
	public float getAmmoutByCurrency(String currency) {
		switch (currency) {
		case "BRL": return CurrencyBRL; 
		case "BYN": return CurrencyBYN;
		case "EUR": return CurrencyEUR;
		case "KZT": return CurrencyKZT;
		case "RUB": return CurrencyRUB;
		case "UAH": return CurrencyUAH;
		case "USD": return CurrencyUSD;
		}
		return 10000000f;
	}
	
	public static DonationType getDonationType(String data) throws JSONException
	{
		JSONObject json = new JSONObject(data);
		DonationType obj = new DonationType();
		if (json.has("active"))
			obj.Active = json.getBoolean("active");
		if (json.has("m"))
		{
			JSONArray jarr = json.getJSONArray("m");
			if (jarr != null)
			{
				obj.Messages = new ArrayList<String>();
				for (int i = 0; i < jarr.length(); i++)
					obj.Messages.add(jarr.getString(i));
			}
		}
		if (json.has("c"))
		{
			JSONArray jarr = json.getJSONArray("c");
			if (jarr != null)
			{
				obj.Commands = new ArrayList<String>();
				for (int i = 0; i < jarr.length(); i++)
					obj.Commands.add(jarr.getString(i));
			}
		}
		if (json.has("BRL"))
			obj.CurrencyBRL = (float) json.getDouble("BRL");
		if (json.has("BYN"))
			obj.CurrencyBYN = (float) json.getDouble("BYN");
		if (json.has("EUR"))
			obj.CurrencyEUR = (float) json.getDouble("EUR");
		if (json.has("KZT"))
			obj.CurrencyKZT = (float) json.getDouble("KZT");
		if (json.has("RUB"))
			obj.CurrencyRUB = (float) json.getDouble("RUB");
		if (json.has("UAH"))
			obj.CurrencyUAH = (float) json.getDouble("UAH");
		if (json.has("USD"))
			obj.CurrencyUSD = (float) json.getDouble("USD");
		if (json.has("name"))
			obj.Name = json.getString("name");
		return obj;
	}
	
	public String toString()
	{
		JSONObject json = new JSONObject();
		try {
			json.put("active", Active);
			json.put("m", Messages);
			json.put("c", Commands);
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
		return json.toString();
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
