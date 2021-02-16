package com.folleach.daintegrate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.folleach.donationalerts.DonationAlertsEvent;
import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;
import com.google.common.collect.Lists;

import net.minecraft.util.text.ChatType;
import net.minecraftforge.common.ForgeHooks;

public class DataCollector {
	public List<DonationAlertsEvent> Donations;
	public TypesManager TManager;
	public ChatType DonationTo;
	public boolean SkipTestDonation;
	public int CountDonationInCache = 30;
	String Token;
	
	private File dataFile;
	private File tokenFile;

	public static final String TagDonationMessage      = "<donation_message>";
	public static final String TagDonationAmount       = "<donation_amount>";
	public static final String TagDonationCurrency     = "<donation_currency>";
	public static final String TagDonationUserName     = "<donation_username>";
	public static final String TagMinecraftPlayerName  = "<minecraft_playername>";

	private static final String PathName = "DAIntegrate/";
	private static final String SettingsFileName = "settings.json";
	private static final String TokenFileName = "TOKEN";
	
	public DataCollector() throws IOException
	{
		Donations = Lists.<DonationAlertsEvent>newArrayList();
		TManager = new TypesManager();
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
		t.AddMessage("<donation_username> - <donation_amount> <donation_currency>");
		t.AddMessage("<donation_message>");
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
			TManager.Load(new JSONObject(json.getJSONObject("typesManager")).getJSONArray("types"));
			DonationTo = ChatType.byId((byte) json.getInt("donationTo"));
			SkipTestDonation = json.getBoolean("skipTest");
			CountDonationInCache = json.getInt("countDonationInCache");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void Save() throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("typesManager", TManager.toJson());
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
	
	public void AddDonation(DonationAlertsEvent donate)
	{
		if (SkipTestDonation && donate.IsTest)
			return;
		Donations.add(donate);
		DonationType executor = null;
		for (int i = 0; i < TManager.getTypes().size(); i++)
		{
			if (donate.Amount >= TManager.get(i).getAmountByCurrency(donate.Currency)
					&& (executor == null || TManager.get(i).getAmountByCurrency(donate.Currency) > executor.getAmountByCurrency(donate.Currency))
					&& TManager.get(i).Active) {
				executor = TManager.get(i);
			}
		}
		if (executor == null)
			return;
		String temp;
		if (executor.getMessages() != null)
			for (int i = 0; i < executor.getMessages().size(); i++)
			{
				temp = ReplaceConstants(executor.getMessages().get(i), donate);
				Main.GameInstance.ingameGUI.getChatGUI().printChatMessage(ForgeHooks.newChatWithLinks(temp));
			}
		if (executor.getCommands() != null)
			for (int i = 0; i < executor.getCommands().size(); i++)
			{
				temp = ReplaceConstants(executor.getCommands().get(i), donate);
				Main.GameInstance.player.sendChatMessage(temp);
			}
		RecountDonationCache();
	}
	
	public void RecountDonationCache()
	{
		while (Donations.size() > CountDonationInCache)
			Donations.remove(0);
	}
	
	private String ReplaceConstants(String pattern, DonationAlertsEvent donat) {
		pattern = pattern.replace(TagDonationMessage, donat.Message);
		pattern = pattern.replace(TagDonationAmount, String.valueOf(donat.Amount));
		pattern = pattern.replace(TagDonationCurrency, String.valueOf(donat.Currency));
		pattern = pattern.replace(TagDonationUserName, String.valueOf(donat.UserName));
		pattern = pattern.replace(TagMinecraftPlayerName, Main.GameInstance.player.getName().getString());
		return pattern;
	}
	
	public void AddDonationType(DonationType t) {
		TManager.getTypes().add(t);
	}

	public void RemoveDonationType(DonationType t) {
		TManager.getTypes().remove(t);
	}

	public void setTManager(TypesManager manager) {
		TManager = manager;
	}

	public boolean isTokenExists() {
		return Token != null && Token.length() > 1;
	}
}
