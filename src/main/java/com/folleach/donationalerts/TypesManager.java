package com.folleach.donationalerts;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
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
		 try {
			json.put("types", types);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		 return json.toString();
	}
}
