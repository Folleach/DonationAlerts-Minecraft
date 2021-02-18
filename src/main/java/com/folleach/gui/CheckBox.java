package com.folleach.gui;

import com.folleach.daintegrate.Main;
import com.folleach.daintegrate.Palette;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CheckBox extends CustomButton
{
	public boolean Flag;
	
	public CheckBox(int x, int y, int widthIn, boolean visibility, String buttonText, boolean flag, IPressable onPress)
    {
        this(x, y, widthIn, 20, buttonText, flag, onPress);
        CheckBox e = this;
		Flag = flag;
	}
	
	public CheckBox(int x, int y, int widthIn, int heightIn, String buttonText, boolean flag, IPressable onPress)
    {
		super(x, y, widthIn, heightIn, buttonText, onPress);
		Flag = flag;
	}

	public void render(MatrixStack matrixs,  int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            // this.mouseDragged(mc, mouseX, mouseY);
            int colorBg = DefaultBackgroundColor;
            int colorFg = DefaultForegroundColor;
            if (!this.active)
            {
            	colorBg = DisableBackgroundColor;
            	colorFg = DisableForegroundColor;
            }
            else if (this.isHovered)
            {
            	colorBg = HoveredBackgroundColor;
            }
            fill(matrixs, x, y, x + width, y + height, colorBg);
            int colorFlag = Palette.RED;
            if (Flag)
            	colorFlag = Palette.GREEN;
            fill(matrixs, x + 4, y + 4, x + 16, y + 16, colorFlag);
            this.drawString(matrixs, fontrenderer, this.getMessage(), this.x + 22, this.y + (this.height / 2) - 4, colorFg);
        }
    }

	public void SwitchFlag(boolean value) {
		Flag = value;
	}
}
