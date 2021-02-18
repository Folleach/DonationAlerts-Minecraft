package com.folleach.donationalerts;

import java.util.List;

import com.folleach.daintegrate.Action;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.collect.Lists;

@OnlyIn(Dist.CLIENT)
public class DonationType {
	public boolean Active;
	
	private List<Action> Actions;

	public String Name = "";
	public float CurrencyBRL;
	public float CurrencyBYN;
	public float CurrencyEUR;
	public float CurrencyKZT;
	public float CurrencyRUB;
	public float CurrencyUAH;
	public float CurrencyUSD;

	public DonationType() {
		Actions = Lists.<Action>newArrayList();
	}

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
	
	public static DonationType getDonationType(JSONObject json) throws JSONException
	{
		DonationType instance = new DonationType();
		if (json.has("active"))
			instance.Active = json.getBoolean("active");
		if (json.has("actions"))
		{
			JSONArray array = json.getJSONArray("actions");
			for (int i = 0; i < array.length(); i++)
				instance.Actions.add(Action.fromJson(array.getJSONObject(i)));
		}
		if (json.has("BRL"))
			instance.CurrencyBRL = (float) json.getDouble("BRL");
		if (json.has("BYN"))
			instance.CurrencyBYN = (float) json.getDouble("BYN");
		if (json.has("EUR"))
			instance.CurrencyEUR = (float) json.getDouble("EUR");
		if (json.has("KZT"))
			instance.CurrencyKZT = (float) json.getDouble("KZT");
		if (json.has("RUB"))
			instance.CurrencyRUB = (float) json.getDouble("RUB");
		if (json.has("UAH"))
			instance.CurrencyUAH = (float) json.getDouble("UAH");
		if (json.has("USD"))
			instance.CurrencyUSD = (float) json.getDouble("USD");
		if (json.has("name"))
			instance.Name = json.getString("name");
		return instance;
	}
	
	public JSONObject toJson()
	{
		JSONObject json = new JSONObject();
		try {
			json.put("active", Active);
			JSONArray actionsArray = new JSONArray();
			for (Action action : Actions)
				actionsArray.put(action.toJson());
			json.put("actions", actionsArray);
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
	
	public List<Action> getActions() {
		return Actions;
	}

	public void addAction(Action action) {
		Actions.add(action);
	}
}
