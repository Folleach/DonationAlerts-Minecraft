package com.folleach.daintegrate;

import com.folleach.donationalerts.DonationAlerts;
import com.folleach.gui.screens.MainScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyHandler
{
	private KeyBinding openWindow;
	private Minecraft game;
	private DataCollector data;
	private DonationAlerts donationAlerts;
	private DonationAlertsIntegrate donationAlertsIntegrate;

	public KeyHandler(Minecraft game,
					  DataCollector data,
					  DonationAlerts donationAlerts,
					  DonationAlertsIntegrate donationAlertsIntegrate) {
		this.game = game;
		this.data = data;
		this.donationAlerts = donationAlerts;
		this.donationAlertsIntegrate = donationAlertsIntegrate;
		openWindow = new KeyBinding("Open window", 61, "Donation Alerts Integrate");
		ClientRegistry.registerKeyBinding(openWindow);
	}

	@SubscribeEvent
	public void PlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START && openWindow.isPressed())
			game.displayGuiScreen(new MainScreen(game.currentScreen, new StringTextComponent("Donation alerts integrate"), donationAlertsIntegrate, donationAlerts, data));
	}
}
