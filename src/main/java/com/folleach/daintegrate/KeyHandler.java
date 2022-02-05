package com.folleach.daintegrate;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KeyHandler
{
	private KeyMapping openWindow;
	
	public KeyHandler()
	{
		openWindow = new KeyMapping("Open window", 1, "Donation Alerts Integrate");
		ClientRegistry.registerKeyBinding(openWindow);
	}

	@SubscribeEvent
	public void PlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START && openWindow.isDown())
			Main.GameInstance.setScreen(new MainWindow(Main.GameInstance, Main.data, Main.da));
	}
}
