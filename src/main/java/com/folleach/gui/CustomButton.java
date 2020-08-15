package com.folleach.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
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

    public CustomButton(int x, int y, int width, boolean visibility, String text, IPressable onPress)
    {
        super(x, y, width, 20, new StringTextComponent(text), onPress);
        this.visible = visibility;
    }

    public CustomButton(int x, int y, int widthIn, int heightIn, String text, IPressable onPress)
    {
        super(x, y, widthIn, heightIn, new StringTextComponent(text), onPress);
    }

    public void drawButton(MatrixStack matrixs,  Minecraft mc, int x, int y, int mouseX, int mouseY, float partialTicks)
	{
		this.x = x;
		this.y = y;
		render(matrixs, mouseX, mouseY, partialTicks);
	}
    
    public void render(MatrixStack matrixs, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible) {
            FontRenderer fontrenderer = Minecraft.getInstance().fontRenderer;
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
