package com.folleach.daintegrate;

import java.io.IOException;
import java.net.URISyntaxException;

import com.folleach.donationalerts.Donation;
import com.folleach.donationalerts.DonationAlerts;
import com.folleach.donationalerts.DonationType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.client.settings.KeyBindingMap;

@SideOnly(Side.CLIENT)
@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.MODVERSION)
public class Main {
	public static final String MODID = "daintegratew";
	public static final String MODNAME = "Donation Alerts Integrate";
	public static final String MODVERSION = "1.1.0";
	public static final String DASERVER = "https://socket.donationalerts.ru:443";
	
	public static Minecraft GameInstance;
	public static DonationAlerts da;
	public static DataCollector data;
	public static KeyHandler keys;
	
	@EventHandler
	public void PreLoad(FMLPreInitializationEvent e) throws IOException {
		
	}
	
	@EventHandler
	public void Initialize(FMLInitializationEvent e) throws IOException
	{
		GameInstance = FMLClientHandler.instance().getClient();
		keys = new KeyHandler();
		data = new DataCollector();
		MinecraftForge.EVENT_BUS.register(keys);
	}
	
	@EventHandler
	public void LoadComplite(FMLLoadCompleteEvent e) throws URISyntaxException
	{
		da = new DonationAlerts(DASERVER);
	}
	
	public static void AddDonation(Donation donat) {
		data.AddDonation(donat);
	}
	
	public static void DonationAlertsInformation(String message) {
		GameInstance.ingameGUI.addChatMessage(ChatType.SYSTEM,
				new TextComponentString("[DonationAlerts] "+message));
	}
}
