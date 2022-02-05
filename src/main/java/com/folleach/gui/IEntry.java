package com.folleach.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IEntry
{
	void drawEntry(PoseStack matrixs, int x, int y, int mouseX, int mouseY, float partialTicks);
	int getHeightE();
	void keyType(char typedChar, int keyCode);
	boolean mouseClick(double mouseX, double mouseY, int mouseButton);
    boolean keyPress(int a, int b, int c);
}
