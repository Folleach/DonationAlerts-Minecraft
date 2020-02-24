package com.folleach.donationalerts;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@OnlyIn(Dist.CLIENT)
public class TypesManager {
	private List<DonationType> types;
	
	public List<DonationType> getTypes() {
		return types;
	}
	
	public DonationType get(int index) {
		return types.get(index);
	}
	
	public TypesManager() {
		types = new ArrayList<DonationType>();
	}
	
	public void Load(JSONArray jarr) throws JSONException
	{
		for (int i = 0; i < jarr.length(); i++)
			types.add(DonationType.getDonationType(jarr.getString(i)));
	}
	
	public String toString()
	{
		 JSONObject json = new JSONObject();
		 JSONArray jsonArray = new JSONArray();
		 try {
		 	for (DonationType item : types)
			{
				jsonArray.put(item.toString());
			}
			json.put("types", jsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return json.toString();
	}
}
