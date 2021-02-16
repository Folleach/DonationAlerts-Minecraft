package com.folleach.daintegrate;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyHandler
{
	private KeyBinding openWindow;
	
	public KeyHandler()
	{
		openWindow = new KeyBinding("Open window", 61, "Donation Alerts Integrate");
		ClientRegistry.registerKeyBinding(openWindow);
	}

	@SubscribeEvent
	public void PlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START && openWindow.isPressed())
			Main.GameInstance.displayGuiScreen(new MainWindow(Main.GameInstance, Main.data, Main.da));
	}
}
