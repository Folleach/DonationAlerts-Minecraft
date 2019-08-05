package com.folleach.gui;

import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraftforge.fml.relauncher.Side;

@SideOnly(Side.CLIENT)
public interface IEntry {
	void drawEntry(int x, int y, int mouseX, int mouseY, float partialTicks);
	int getHeight();
	void keyTyped(char typedChar, int keyCode);
	void mouseClicked(int mouseX, int mouseY, int mouseButton);
}
