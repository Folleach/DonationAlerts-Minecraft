package com.folleach.donationalerts;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.json.JSONException;
import org.json.JSONObject;

@OnlyIn(Dist.CLIENT)
public class AdditionalData {
	public String ForceVariations;
	public int Randomness;
	
	public static AdditionalData getAdditionalData(String data)
	{
		AdditionalData ad = new AdditionalData();
		JSONObject json;
		try {
			json = new JSONObject(data);
			if (json.has("force_variation"))
				ad.ForceVariations = json.getString("force_variation");
			ad.Randomness = json.getInt("randomness");
		} catch (JSONException e) {
			return null;
		}
		return ad;
	}
}
