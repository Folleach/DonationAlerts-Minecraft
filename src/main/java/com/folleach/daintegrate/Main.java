package com.folleach.daintegrate;

import java.io.IOException;
import java.net.URISyntaxException;

import com.folleach.donationalerts.AlertType;
import com.folleach.donationalerts.DonationAlertsEvent;
import com.folleach.donationalerts.DonationAlerts;

import com.folleach.daintegrate.executors.CommandExecutor;
import com.folleach.daintegrate.executors.MessageExecutor;
import net.minecraft.client.Minecraft;
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

	public DonationAlertsIntegrate donationAlertsIntegrate;

	private DonationAlerts donationAlerts;
	private DataCollector data;
	private KeyHandler keys;

	public Main()
	{
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::LoadComplete);
	}

	public void LoadComplete(final FMLLoadCompleteEvent event)
	{
		Minecraft game = Minecraft.getInstance();

		try {
			data = new DataCollector();
			donationAlertsIntegrate = new DonationAlertsIntegrate(game, data);
			donationAlerts = new DonationAlerts(DASERVER, donationAlertsIntegrate);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		donationAlertsIntegrate.addExecutor(new MessageExecutor(game));
		donationAlertsIntegrate.addExecutor(new CommandExecutor(game));
		keys = new KeyHandler(game, data, donationAlerts, donationAlertsIntegrate);
		MinecraftForge.EVENT_BUS.register(keys);
	}
	
	public static void DonationAlertsInformation(String message)
	{
		Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("[DonationAlerts] " + message));
	}
}
