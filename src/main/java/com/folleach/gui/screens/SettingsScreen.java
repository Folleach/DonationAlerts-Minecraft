package com.folleach.gui.screens;

import com.folleach.daintegrate.DataCollector;
import com.folleach.daintegrate.DonationAlertsIntegrate;
import com.folleach.daintegrate.Palette;
import com.folleach.gui.CheckBox;
import com.folleach.gui.CustomTextBox;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;

public class SettingsScreen extends TitledScreen {
    private String langDonto = I18n.format("daintegratew.donto") + ":";

    private FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    private CheckBox SETTINGSBUTTON_SkippingTestDonation;
    private CheckBox SETTINGSBUTTON_DTChat;
    private CheckBox SETTINGSBUTTON_DTSystem;
    private CheckBox SETTINGSBUTTON_DTGameInfo;
    private CustomTextBox SETTINGS_ShowCountDonation;
    private DataCollector data;
    private DonationAlertsIntegrate donationAlertsIntegrate;

    protected SettingsScreen(Screen parent, DataCollector data, DonationAlertsIntegrate donationAlertsIntegrate) {
        super(parent, new StringTextComponent(I18n.format("daintegratew.settings")), true);
        this.data = data;
        this.donationAlertsIntegrate = donationAlertsIntegrate;
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);

        SETTINGSBUTTON_SkippingTestDonation = this.addButton(new CheckBox(125, 25, 170,true, I18n.format("daintegratew.skiptestd"), data.SkipTestDonation, this::SettingsSkipTestDonationClick));
        SETTINGSBUTTON_DTChat = this.addButton(new CheckBox(125, 65, 80, true, I18n.format("daintegratew.chat"), data.DonationTo == ChatType.CHAT, this::SettingsDTChatClick));
        SETTINGSBUTTON_DTSystem = this.addButton(new CheckBox(210, 65, 80, true, I18n.format("daintegratew.system"), data.DonationTo == ChatType.SYSTEM, this::SettingsDTSystemClick));
        SETTINGSBUTTON_DTGameInfo = this.addButton(new CheckBox(295, 65, 80, true, I18n.format("daintegratew.gameinfo"), data.DonationTo == ChatType.GAME_INFO, this::SettingsDTGameClick));
        SETTINGS_ShowCountDonation = new CustomTextBox(fontRenderer, 125, 90, 200, 20, "");
        SETTINGS_ShowCountDonation.tag = I18n.format("daintegratew.countshowdonation");
        SETTINGS_ShowCountDonation.setText(Integer.toString(data.CountDonationInCache));
    }

    @Override
    public void renderScreenContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        SETTINGS_ShowCountDonation.renderButton(matrixStack);
        drawString(matrixStack, fontRenderer, langDonto, 125, 54, Palette.AlmostWhiteButNotWhite);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return SETTINGS_ShowCountDonation.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return SETTINGS_ShowCountDonation.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return SETTINGS_ShowCountDonation.mouseClicked(mouseX, mouseY, button);
    }

    private void SettingsSkipTestDonationClick(Button button)
    {
        CheckBox chb;
        chb = (CheckBox)button;
        chb.SwitchFlag(!chb.Flag);
        data.SkipTestDonation = (chb.Flag);
    }

    private void SettingsDTChatClick(Button button)
    {
        CheckBox chb;
        SETTINGSBUTTON_DTSystem.SwitchFlag(false);
        SETTINGSBUTTON_DTGameInfo.SwitchFlag(false);
        chb = (CheckBox)button;
        chb.SwitchFlag(true);
        data.DonationTo = ChatType.CHAT;
    }

    private void SettingsDTSystemClick(Button button)
    {
        CheckBox chb;
        SETTINGSBUTTON_DTChat.SwitchFlag(false);
        SETTINGSBUTTON_DTGameInfo.SwitchFlag(false);
        chb = (CheckBox)button;
        chb.SwitchFlag(true);
        data.DonationTo = ChatType.SYSTEM;
    }

    private void SettingsDTGameClick(Button button)
    {
        CheckBox chb;
        SETTINGSBUTTON_DTChat.SwitchFlag(false);
        SETTINGSBUTTON_DTSystem.SwitchFlag(false);
        chb = (CheckBox)button;
        chb.SwitchFlag(true);
        data.DonationTo = ChatType.GAME_INFO;
    }

    @Override
    public void save() {
        try {
            data.CountDonationInCache = Integer.parseInt(SETTINGS_ShowCountDonation.getText());
            if (data.CountDonationInCache < 1)
                data.CountDonationInCache = 1;
            data.Save();
            donationAlertsIntegrate.RecountDonationCache();
            SETTINGS_ShowCountDonation.LineColor = Palette.GRAY30_TRANSPARENT_xDD;
            SETTINGS_ShowCountDonation.setText(Integer.toString(data.CountDonationInCache));
            setInfoMessage(I18n.format("daintegratew.saved"));
        } catch (NumberFormatException e) {
            SETTINGS_ShowCountDonation.LineColor = Palette.RED;
            setInfoMessage(I18n.format("daintegratew.error"));
        } catch (Exception e) {
            e.printStackTrace();
            setInfoMessage(I18n.format("daintegratew.error"));
        }
    }
}
