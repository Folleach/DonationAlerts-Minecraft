package com.folleach.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IEntry
{
	void drawEntry(MatrixStack matrixs, int x, int y, int mouseX, int mouseY, float partialTicks);
	int getHeight();
	void keyType(char typedChar, int keyCode);
	boolean mouseClick(double mouseX, double mouseY, int mouseButton);
    boolean keyPress(int a, int b, int c);
}
