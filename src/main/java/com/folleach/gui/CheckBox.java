package com.folleach.gui;

import com.folleach.daintegrate.Pallete;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CheckBox extends CustomButton {
	public boolean Flag;
	
	public CheckBox(int buttonId, int x, int y, int widthIn, boolean visibility, String buttonText, boolean flag) {
		super(buttonId, x, y, widthIn, visibility, buttonText);
		Flag = flag;
	}
	
	public CheckBox(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, boolean flag) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		Flag = flag;
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            
            this.mouseDragged(mc, mouseX, mouseY);
            int colorBg = DefaultBackgroundColor;
            int colorFg = DefaultForegroundColor;
            if (!this.enabled)
            {
            	colorBg = DisableBackgroundColor;
            	colorFg = DisableForegroundColor;
            }
            else if (this.hovered)
            {
            	colorBg = HoveredBackgroundColor;
            }
            this.drawRect(x, y, x + width, y + height, colorBg);
            int colorFlag = Pallete.RED;
            if (Flag)
            	colorFlag = Pallete.GREEN;
            this.drawRect(x + 4, y + 4, x + 16, y + 16, colorFlag);
            this.drawString(fontrenderer, this.displayString, this.x + 22, this.y + (this.height / 2) - 4, colorFg);
        }
    }

	public void SwitchFlag(boolean value) {
		Flag = value;
	}
}
