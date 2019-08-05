package com.folleach.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomButton extends GuiButton {
	public int DefaultBackgroundColor = 0x30000000;
	public int DefaultForegroundColor = 0xFFFFFFFF;
	public int HoveredBackgroundColor = 0x60000000;
	public int HoveredForegroundColor = 0xFFF59907;
	public int DisableBackgroundColor = DefaultBackgroundColor;
	public int DisableForegroundColor = 0xFFA0A0A0;
	
    public CustomButton(int buttonId, int x, int y, int width, boolean visibility, String buttonText)
    {
        super(buttonId, x, y, width, 20, buttonText);
        this.visible = visibility;
    }

    public CustomButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void drawButton(Minecraft mc, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		this.x = x;
		this.y = y;
		drawButton(mc, mouseX, mouseY, partialTicks);
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
            	colorFg = HoveredForegroundColor;
            }
            this.drawRect(x, y, x + width, y + height, colorBg);
            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, colorFg);
        }
    }
    
    public boolean isClick(int mouseX, int mouseY) {
        return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }
}
