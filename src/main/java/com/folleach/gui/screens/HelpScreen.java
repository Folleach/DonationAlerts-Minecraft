package com.folleach.gui.screens;

import com.folleach.daintegrate.DonationAlertsIntegrate;
import com.folleach.daintegrate.Palette;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;

import java.util.List;

public class HelpScreen extends TitledScreen {
    private static List<String> langHelpLines;

    static {
        InitializeLangHelpLines();
    }

    public HelpScreen(Screen parent) {
        super(parent, new StringTextComponent(I18n.format("daintegratew.help")), false);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        for (int i = 0; i < langHelpLines.size(); i++)
            font.drawString(matrixStack, langHelpLines.get(i), 10, i * 10 + 30, Palette.WHITE);
    }

    private static void InitializeLangHelpLines() {
        langHelpLines = Lists.newArrayList();
        langHelpLines.add(I18n.format("daintegratew.help.title"));
        langHelpLines.add(DonationAlertsIntegrate.TagDonationMessage + " - " + I18n.format("daintegratew.help.d1"));
        langHelpLines.add(DonationAlertsIntegrate.TagDonationAmount + " - " + I18n.format("daintegratew.help.d2"));
        langHelpLines.add(DonationAlertsIntegrate.TagDonationCurrency + " - " + I18n.format("daintegratew.help.d3"));
        langHelpLines.add(DonationAlertsIntegrate.TagDonationUserName + " - " + I18n.format("daintegratew.help.d4"));
        langHelpLines.add(DonationAlertsIntegrate.TagMinecraftPlayerName + " - " + I18n.format("daintegratew.help.m1"));
        langHelpLines.add("");
        langHelpLines.add(I18n.format("daintegratew.help.projson"));
    }
}
