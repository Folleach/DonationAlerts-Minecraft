package com.folleach.daintegrate;

import java.io.IOException;
import java.net.URISyntaxException;

import com.folleach.donationalerts.Donation;
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
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::Setup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ClientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::EnqueueIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::ProcessIMC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::LoadComplite);
	}

	public void Setup(final FMLCommonSetupEvent event)
	{

	}

	public void ClientSetup(final FMLClientSetupEvent event) {
	}

	public void EnqueueIMC(final InterModEnqueueEvent event)
	{

	}

	public void ProcessIMC(final InterModProcessEvent event)
	{

	}

	public void LoadComplite(final FMLLoadCompleteEvent event)
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
	
	public static void AddDonation(Donation donate)
	{
		data.AddDonation(donate);
	}
	
	public static void DonationAlertsInformation(String message)
	{
		GameInstance.ingameGUI.addChatMessage(ChatType.SYSTEM,
				new StringTextComponent("[DonationAlerts] " + message));
	}
}
