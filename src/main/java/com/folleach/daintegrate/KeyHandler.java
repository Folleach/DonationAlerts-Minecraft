package com.folleach.daintegrate;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class KeyHandler
{
	private KeyMapping openWindow;
	
	public KeyHandler()
	{
		openWindow = new KeyMapping("Open window", 61, "Donation Alerts Integrate");
		ClientRegistry.registerKeyBinding(openWindow);
	}

	@SubscribeEvent
	public void PlayerTick(TickEvent.PlayerTickEvent event)
	{
		if (event.phase == TickEvent.Phase.START && openWindow.isDown() && Thread.currentThread().getName().contains("Render thread"))
			Main.GameInstance.setScreen(new MainWindow(Main.GameInstance, Main.data, Main.da));
	}
}
