package com.folleach.daintegrate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.folleach.donationalerts.Donation;
import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;
import com.folleach.gui.CustomTextBox;
import com.google.common.collect.Lists;
import com.google.common.io.ByteProcessor;

import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.reflect.io.Directory;

@SideOnly(Side.CLIENT)
public class DataCollector {
	public List<Donation> Donations;
	public TypesManager TManager;
	public ChatType DonationTo;
	public boolean SkipTestDonation;
	public int CountDonationInCache = 30;
	public String FEmptyString;
	
	private File dataFile;
	private static int key = 175;
	
	public static final String TagDonationMessage      = "<donation_message>";
	public static final String TagDonationAmount       = "<donation_amount>";
	public static final String TagDonationCurrency     = "<donation_currency>";
	public static final String TagDonationUserName     = "<donation_username>";
	public static final String TagMinecraftPlayerName  = "<minecraft_playername>";
	
	public DataCollector() throws IOException
	{
		Donations = Lists.<Donation>newArrayList();
		TManager = new TypesManager();
		DonationTo = ChatType.CHAT;
		SkipTestDonation = false;
		FEmptyString = "";
		dataFile = new File("DAIntegrated/a.bin");
		
		if (dataFile.exists()) {
			Load();
			return;
		}
		Directory d = new Directory(new File("DAIntegrated/"));
		if (!d.exists())
			d.createDirectory(true, false);
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
	
	//Load --------------
	public void Load() {
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(Paths.get(dataFile.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) (bytes[i] ^ ((key + i) % 256));
		String data = new String(bytes, StandardCharsets.UTF_8);
		JSONObject json;
		try {
			json = new JSONObject(data);
			TManager.Load(new JSONObject(json.getString("tmanager")).getJSONArray("types"));
			DonationTo = ChatType.byId((byte) json.getInt("donationTo"));
			SkipTestDonation = json.getBoolean("skiptest");
			FEmptyString = json.getString("tkn");
			CountDonationInCache = json.getInt("cdic");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	//Save --------------
	public void Save() throws JSONException, IOException {
		JSONObject json = new JSONObject();
		json.put("tmanager", TManager.toString());
		json.put("donationTo", DonationTo.getId());
		json.put("skiptest", SkipTestDonation);
		json.put("tkn", FEmptyString);
		json.put("cdic", CountDonationInCache);
		
		String sj = json.toString();
		byte[] bytes = sj.getBytes(StandardCharsets.UTF_8);
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) (bytes[i] ^ ((key + i) % 256));
		try (FileOutputStream stream = new FileOutputStream(dataFile.getPath())) {
		    stream.write(bytes);
		}
	}
	
	public void AddDonation(Donation donat)
	{
		if (SkipTestDonation && donat.IsTest)
			return;
		Donations.add(donat);
		DonationType executor = null;
		for (int i = 0; i < TManager.getTypes().size(); i++)
		{
			if (donat.Amount >= TManager.get(i).getAmmoutByCurrency(donat.Currency)
					&& (executor == null || TManager.get(i).getAmmoutByCurrency(donat.Currency) > executor.getAmmoutByCurrency(donat.Currency))
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
				temp = ReplaceConstants(executor.getMessages().get(i), donat);
				Main.GameInstance.ingameGUI.addChatMessage(DonationTo, ForgeHooks.newChatWithLinks(temp));
			}
		if (executor.getCommands() != null)
			for (int i = 0; i < executor.getCommands().size(); i++)
			{
				temp = ReplaceConstants(executor.getCommands().get(i), donat);
				Main.GameInstance.player.sendChatMessage(temp);
			}
		RecountDonationCache();
	}
	
	public void RecountDonationCache()
	{
		while (Donations.size() > CountDonationInCache)
			Donations.remove(0);
	}
	
	String ReplaceConstants(String pattern, Donation donat) {
		pattern = pattern.replace(TagDonationMessage, donat.Message);
		pattern = pattern.replace(TagDonationAmount, String.valueOf(donat.Amount));
		pattern = pattern.replace(TagDonationCurrency, String.valueOf(donat.Currency));
		pattern = pattern.replace(TagDonationUserName, String.valueOf(donat.UserName));
		pattern = pattern.replace(TagMinecraftPlayerName, Main.GameInstance.player.getName());
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
		return FEmptyString == null ? false : FEmptyString.length() > 1;
	}
}
