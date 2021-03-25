package com.folleach.gui.screens;

import com.folleach.daintegrate.DataCollector;
import com.folleach.daintegrate.Main;
import com.folleach.daintegrate.Palette;
import com.folleach.donationalerts.DonationAlerts;
import com.folleach.gui.CustomTextBox;
import com.folleach.gui.DefaultButton;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import org.json.JSONException;

import java.io.IOException;

public class ConnectionScreen extends TitledScreen {
    private static String langTokenExist;
    private static String langTokenNotExists;
    private static String langConnected;
    private static String langDisconnected;
    private static String langToken;
    private static String langConnectState;

    private static final int TOKENPANELY = 30;

    private CustomTextBox tokenTextBox;

    private DefaultButton STATUSBUTTON_Delete;
    private DefaultButton STATUSBUTTON_ConnectionController;

    private static int lengthConnectionState;

    static {
        langTokenExist = I18n.format("daintegratew.token.exists")+".";
        langTokenNotExists = I18n.format("daintegratew.token.absents")+".";
        langConnected = I18n.format("daintegratew.connected")+".";
        langDisconnected = I18n.format("daintegratew.disconnected")+".";
        langToken = I18n.format("daintegratew.token")+":";
        langConnectState = I18n.format("daintegratew.connstatus")+":";
    }

    private DonationAlerts donationAlerts;
    private DataCollector data;

    public ConnectionScreen(Screen parent, DonationAlerts donationAlerts, DataCollector data) {
        super(parent, new StringTextComponent(I18n.format("daintegratew.status")), true);
        this.donationAlerts = donationAlerts;
        this.data = data;
    }

    @Override
    protected void init() {
        super.init();

        tokenTextBox = new CustomTextBox(font, 10, TOKENPANELY + 50, 200, 20, "");
        tokenTextBox.setTextColor(Palette.WHITE);
        tokenTextBox.tag = langToken;

        STATUSBUTTON_Delete = this.addButton(new DefaultButton(10, TOKENPANELY + 70, 120, true, I18n.format("daintegratew.delete"), this::statusDeleteClick));
        STATUSBUTTON_ConnectionController = this.addButton(new DefaultButton(10, TOKENPANELY + 10, 120, true,
                donationAlerts.getConnected() ? I18n.format("daintegratew.disconnect") : I18n.format("daintegratew.connect"), this::connectionControllerClick));

        lengthConnectionState = font.getStringWidth(langConnectState);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        font.drawString(matrixStack, langConnectState, 10, 30, Palette.WHITE);
        font.drawString(matrixStack, donationAlerts.getConnected() ? langConnected : langDisconnected, 10 + lengthConnectionState + 5, 30,
                donationAlerts.getConnected() ? Palette.GREEN : Palette.RED);

        tokenTextBox.renderButton(matrixStack);
        font.drawString(matrixStack, data.isTokenExists() ? langTokenExist : langTokenNotExists, 10, TOKENPANELY + 70, data.isTokenExists() ? Palette.GREEN : Palette.RED);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        tokenTextBox.keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        return tokenTextBox.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        super.charTyped(codePoint, modifiers);
        return tokenTextBox.charTyped(codePoint, modifiers);
    }

    @Override
    public void save() {
        data.Token = tokenTextBox.getText();
        try {
            data.Save();
            setInfoMessage(I18n.format("daintegratew.saved"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            setInfoMessage(I18n.format("daintegratew.error"));
        }
    }

    private void statusDeleteClick(Button button)
    {
        tokenTextBox.setText("");
        save();
    }

    private void connectionControllerClick(Button button)
    {
        if (donationAlerts.getConnected()) {
            donationAlerts.Disconnect();
            STATUSBUTTON_ConnectionController.setMessage(new StringTextComponent(I18n.format("daintegratew.connect")));
        }
        else {
            try {
                donationAlerts.Connect(data.Token);
            } catch (JSONException e) {
                Main.DonationAlertsInformation(I18n.format("daintegratew.error"));
                e.printStackTrace();
            }
            STATUSBUTTON_ConnectionController.setMessage(new StringTextComponent(I18n.format("daintegratew.disconnect")));
        }
    }
}
