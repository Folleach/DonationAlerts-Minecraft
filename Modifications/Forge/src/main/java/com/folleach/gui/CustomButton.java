package com.folleach.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomButton extends Button
{
	public int DefaultBackgroundColor = 0x30000000;
	public int DefaultForegroundColor = 0xFFFFFFFF;
	public int HoveredBackgroundColor = 0x60000000;
	public int HoveredForegroundColor = 0xFFF59907;
	public int DisableBackgroundColor = DefaultBackgroundColor;
	public int DisableForegroundColor = 0xFFA0A0A0;

    public CustomButton(int x, int y, int width, boolean visibility, String text, Button.OnPress onPress)
    {
        super(x, y, width, 20, new TextComponent(text), onPress);
        this.visible = visibility;
    }

    public CustomButton(int x, int y, int widthIn, int heightIn, String text, Button.OnPress onPress)
    {
        super(x, y, widthIn, heightIn, new TextComponent(text), onPress);
    }

    public void drawButton(PoseStack matrixs, int mouseX, int mouseY, float partialTicks)
    {
        render(matrixs, mouseX, mouseY, partialTicks);
    }

    public void drawButton(PoseStack matrixs, Minecraft mc, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		this.x = x;
		this.y = y;
		render(matrixs, mouseX, mouseY, partialTicks);
	}

    public void render(PoseStack matrixs, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible) {
            Font fontrenderer = Minecraft.getInstance().font;
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            // this.mouseDragged(mouseX, mouseY, partialTicks);
            int colorBg = DefaultBackgroundColor;
            int colorFg = DefaultForegroundColor;
            if (!this.active) {
                colorBg = DisableBackgroundColor;
                colorFg = DisableForegroundColor;
            } else if (this.isHovered) {
                colorBg = HoveredBackgroundColor;
                colorFg = HoveredForegroundColor;
            }
            fill(matrixs, x, y, x + width, y + height, colorBg);
            this.drawCenteredString(matrixs, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, colorFg);
        }
    }
}
