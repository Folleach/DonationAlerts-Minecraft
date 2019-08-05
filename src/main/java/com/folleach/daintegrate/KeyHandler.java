package com.folleach.daintegrate;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyHandler {
	KeyBinding openWindow;
	
	public KeyHandler()
	{
		openWindow = new KeyBinding("Open window", Keyboard.KEY_EQUALS, "Donation Alerts Integrate");
		ClientRegistry.registerKeyBinding(openWindow);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void PlayerTick(PlayerTickEvent e)
	{
		if (e.side == Side.SERVER)
			return;
		if (e.phase == Phase.START && openWindow.isPressed())
		{
			Main.GameInstance.displayGuiScreen(new MainWindow(Main.GameInstance, Main.data, Main.da));
		}
	}
}
