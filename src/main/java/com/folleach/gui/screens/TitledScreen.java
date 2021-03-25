package com.folleach.gui.screens;

import com.folleach.daintegrate.Main;
import com.folleach.daintegrate.Palette;
import com.folleach.gui.ChapterButton;
import com.folleach.gui.CustomButton;
import com.folleach.gui.DefaultButton;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TitledScreen extends Screen {
    private Screen parent;
    private String title;
    private boolean savable;

    private int infoMessageTime;
    private String infoMessage;
    private int infoMessageWidth;
    private final int defaultMessageTime = 60;

    protected TitledScreen(Screen parent, ITextComponent titleIn, boolean savable) {
        super(titleIn);
        this.parent = parent;
        title = titleIn.getString();
        this.savable = savable;
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        addButton(new ChapterButton(4, 0,
                "",
                new ResourceLocation(Main.MODID, "textures/icon_back.png"),
                b -> DisplayParent(),
                20));

        addButton(new DefaultButton(width - 120, 0, 120, savable, I18n.format("daintegratew.save"), (b) -> save()));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        fill(matrixStack, 0, 20, width, height, Palette.Background);
        renderScreenContent(matrixStack, mouseX, mouseY, partialTicks);
        fill(matrixStack, 0, 0, width, 20, Palette.Grayish);
        font.drawStringWithShadow(matrixStack, title, 30, 6, Palette.AlmostWhiteButNotWhite);

        if (infoMessageTime > 0) {
            int x = width - infoMessageWidth - 125;
            font.drawStringWithShadow(matrixStack, infoMessage, x, 6, Palette.AlmostWhiteButNotWhite);
            infoMessageTime--;
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void setInfoMessage(String message) {
        infoMessageWidth = font.getStringWidth(message);
        infoMessageTime = defaultMessageTime;
        infoMessage = message;
    }

    public void renderScreenContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    }

    public void save() {
    }

    public boolean canSave() {
        return savable;
    }

    @Override
    public void onClose() {
        super.onClose();

        if (canSave()) {
            save();
        }
    }

    @Override
    public void closeScreen() {
        DisplayParent();
    }

    private void DisplayParent() {
        Minecraft.getInstance().displayGuiScreen(parent);
    }
}
