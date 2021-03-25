package com.folleach.daintegrate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;

import net.minecraft.util.text.ChatType;

public class DataCollector {
	public TypesManager typesManager;
	public ChatType DonationTo;
	public boolean SkipTestDonation;
	public int CountDonationInCache = 30;
	public String Token;
	
	private File dataFile;
	private File tokenFile;

	private static final String PathName = "DAIntegrate/";
	private static final String SettingsFileName = "settings.json";
	private static final String TokenFileName = "TOKEN";
	
	public DataCollector() throws IOException
	{
		typesManager = new TypesManager();
		DonationTo = ChatType.CHAT;
		SkipTestDonation = false;
		Token = "";
		dataFile = new File(PathName, SettingsFileName);
		tokenFile = new File(PathName, TokenFileName);

		if (dataFile.exists()) {
			Load();
			return;
		}
		File d = new File(PathName);
		if (!d.exists())
			d.mkdir();
		dataFile.createNewFile();
		DonationType t = new DonationType();
		t.Active = true;
		t.Name = "Default";
		t.addAction(new Action(
				"message",
				0,
				new JSONObject().put("message", "<donation_username> - <donation_amount> <donation_currency>")));
		t.addAction(new Action(
				"message",
				0,
				new JSONObject().put("message", "<donation_message>")));
		AddDonationType(t);
		try {
			Save();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void Load() throws IOException {
		byte[] bytes = null;
		bytes = Files.readAllBytes(Paths.get(dataFile.getPath()));
		if (tokenFile.exists())
			Token = new String(Files.readAllBytes(Paths.get(tokenFile.getPath())), StandardCharsets.UTF_8);
		String data = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json;
		try {
			json = new JSONObject(data);
			typesManager.Load(json.getJSONObject("typesManager").getJSONArray("types"));
			DonationTo = ChatType.byId((byte) json.getInt("donationTo"));
			SkipTestDonation = json.getBoolean("skipTest");
			CountDonationInCache = json.getInt("countDonationInCache");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void Save() throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("typesManager", typesManager.toJson());
		json.put("donationTo", DonationTo.getId());
		json.put("skipTest", SkipTestDonation);
		json.put("countDonationInCache", CountDonationInCache);

		try (FileOutputStream stream = new FileOutputStream(tokenFile.getPath())) {
			stream.write(Token.getBytes(StandardCharsets.UTF_8));
		}
		
		String sj = json.toString();
		byte[] bytes = sj.getBytes(StandardCharsets.UTF_8);
		try (FileOutputStream stream = new FileOutputStream(dataFile.getPath())) {
		    stream.write(bytes);
		}
	}
	
	public void AddDonationType(DonationType t) {
		typesManager.getTypes().add(t);
	}

	public void RemoveDonationType(DonationType t) {
		typesManager.getTypes().remove(t);
	}

	public void setTypesManager(TypesManager manager) {
		typesManager = manager;
	}

	public boolean isTokenExists() {
		return Token != null && Token.length() > 1;
	}
}
