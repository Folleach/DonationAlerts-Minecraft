package com.folleach.gui;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IEntry
{
	void drawEntry(int x, int y, int mouseX, int mouseY, float partialTicks);
	int getHeight();
	void keyTyped(char typedChar, int keyCode);
	boolean mouseClicked(double mouseX, double mouseY, int mouseButton);
    boolean keyPressed(int a, int b, int c);
}
