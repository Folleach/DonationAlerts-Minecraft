package com.folleach.donationalerts;

import org.json.JSONException;
import org.json.JSONObject;

import com.folleach.daintegrate.Main;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
