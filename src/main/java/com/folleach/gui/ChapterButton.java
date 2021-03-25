package com.folleach.gui;


import com.folleach.daintegrate.Palette;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ChapterButton extends Button {

    private ResourceLocation icon;
    private final int iconWidth = 24;
    private final int iconHeight = 24;
    private Minecraft minecraft;

    public ChapterButton(int x, int y, String label, ResourceLocation icon, IPressable pressedAction) {
        this(x, y, label, icon, pressedAction, 120);
    }

    public ChapterButton(int x, int y, String label, ResourceLocation icon, IPressable pressedAction, int width) {
        super(x, y, width, 20, new StringTextComponent(label), pressedAction);
        this.icon = icon;
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        FontRenderer font = Minecraft.getInstance().fontRenderer;
        fill(matrixStack, x, y, x + width, y + height, Palette.Grayish);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.icon);
        blit(matrixStack, this.x + 4, this.y + 4, 0, 0, iconWidth / 2, iconHeight / 2, iconWidth / 2, iconHeight / 2);

        font.drawStringWithShadow(matrixStack, super.getMessage().getString(), x + 24, y + 6, isHovered ? Palette.BrandOrange : Palette.AlmostWhiteButNotWhite);
    }
}
