package com.folleach.daintegrate;

import java.io.IOException;
import java.net.URISyntaxException;

import com.folleach.donationalerts.AlertType;
import com.folleach.donationalerts.DonationAlertsEvent;
import com.folleach.donationalerts.DonationAlerts;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "daintegratew";
	public static final String MODNAME = "Donation Alerts Integrate";
	public static final String MODVERSION = "1.1.0";
	public static final String DASERVER = "https://socket.donationalerts.ru:443";
	
	public static Minecraft GameInstance;
	public static DonationAlerts da;
	public static DataCollector data;
	public static KeyHandler keys;

	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::LoadComplete);
	}

	public void LoadComplete(final FMLLoadCompleteEvent event)
	{
		GameInstance = Minecraft.getInstance();
		keys = new KeyHandler();
		try {
			data = new DataCollector();
			da = new DonationAlerts(DASERVER);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		MinecraftForge.EVENT_BUS.register(keys);
	}
	
	public static void AddDonation(DonationAlertsEvent event)
	{
		if (event.Type == AlertType.Donate)
			data.AddDonation(event);
		else
			DonationAlertsInformation("Invalid type: " + event.Type.toString());
	}
	
	public static void DonationAlertsInformation(String message)
	{
		GameInstance.ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("[DonationAlerts] " + message));
	}
}
