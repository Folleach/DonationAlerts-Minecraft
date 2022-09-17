package com.folleach.daintegrate;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.folleach.gui.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.folleach.dontaionalerts.DonationAlertsCore;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.json.JSONException;

import com.folleach.donationalerts.DonationAlerts;
import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;

@OnlyIn(Dist.CLIENT)
public class MainWindow extends Screen
{
	enum PanelsType { Messages, Status, Types, Settings, Help }
	
	private static final int SPACE_LEFTBUTTON = 20;
	private static final int TOKENPANELY = 60;
	
	private static String langDonto;
	private static String langToken;
	private static String langTokenExist;
	private static String langTokenNotExists;
	private static String langConnected;
	private static String langDisonnected;
	private static String langConnectState;
	private static String langSaved;
	private static String langError;
	private static List<String> langHelpLines;
	private static int lenghtConnectState;	
	private static int lenghtSaved;
	
	private Minecraft mc;
	private Font fontRenderer;
	private DataCollector data;
	private DonationAlerts dalets;
	private PanelsType activePanel;
	
	private CustomButton LEFTBUTTON_Messages;
	private CustomButton LEFTBUTTON_Status;
	private CustomButton LEFTBUTTON_Types;
	private CustomButton LEFTBUTTON_Settings;
	private CustomButton LEFTBUTTON_Help;
	private CustomButton LEFTBUTTON_SupportAuthor;
	
	private DefaultButton STATUSBUTTON_Save;
	private DefaultButton STATUSBUTTON_Delete;
	private DefaultButton STATUSBUTTON_ConnectionController;
	// private GuiLabel STATUSLABEL_Warning;
	
	private DefaultButton TYPESBUTTON_Save;
	private CustomButton TYPESBUTTON_Add;
	
	private CheckBox SETTINGSBUTTON_SkippingTestDonation;
	private CheckBox SETTINGSBUTTON_DTChat;
	private CheckBox SETTINGSBUTTON_DTSystem;
	private CheckBox SETTINGSBUTTON_DTGameInfo;
	private CustomTextBox SETTINGS_ShowCountDonation;
	private DefaultButton SETTINGSBUTTON_Save;
	
	private CustomTextBox text1;
	
	private ScrollPanel<MessageEntry> messagesPanel;
	private ScrollPanel<DonationTypeEntry> typesPanel;
	
	private boolean typesSuccessfulSave;
	private int typesSaveTimer;
	private boolean initialized;

	public MainWindow(Minecraft mc, DataCollector d, DonationAlerts da)
	{
		super(new TextComponent("MainWindow"));
		this.mc = mc;
		fontRenderer = mc.font;
		data = d;
		dalets = da;
		InitializeStrings();
	}
	
	private void InitializeStrings()
	{
		langDonto = I18n.get("daintegratew.donto")+":";
		langToken = I18n.get("daintegratew.token")+":";
		langTokenExist = I18n.get("daintegratew.token.exists")+".";
		langTokenNotExists = I18n.get("daintegratew.token.absents")+".";
		langConnected = I18n.get("daintegratew.connected")+".";
		langDisonnected = I18n.get("daintegratew.disconnected")+".";
		langConnectState = I18n.get("daintegratew.connstatus")+":";
		langSaved = I18n.get("daintegratew.saved");
		langError = I18n.get("daintegratew.error");
		lenghtSaved = mc.font.width(langSaved) + 10;
	}

    public void init() {
		initialized = false;
		this.children().clear();
		LEFTBUTTON_Messages = this.addWidget(new CustomButton(0, 20, 120, 20, I18n.get("daintegratew.messages"), this::SwitchPanel_Messages));
		LEFTBUTTON_Status = this.addWidget(new CustomButton(0, 20 + SPACE_LEFTBUTTON, 120, 20, I18n.get("daintegratew.status"), this::SwitchPanel_Status));
		LEFTBUTTON_Types = this.addWidget(new CustomButton(0, 20 + (2 * SPACE_LEFTBUTTON), 120, 20, I18n.get("daintegratew.types"), this::SwitchPanel_Types));
		LEFTBUTTON_Settings = this.addWidget(new CustomButton(0, 20 + (3 * SPACE_LEFTBUTTON), 120, 20, I18n.get("daintegratew.settings"), this::SwitchPanel_Settings));
		LEFTBUTTON_Help = this.addWidget(new CustomButton(0, 20 + (4 * SPACE_LEFTBUTTON), 120, 20, I18n.get("daintegratew.help"), this::SwitchPanel_Help));
		LEFTBUTTON_SupportAuthor = this.addWidget(new DefaultButton(0, height - 20, 120, 20, I18n.get("daintegratew.suppauthor"), this::SwitchPanel_SupportAuthor));
		SETTINGSBUTTON_SkippingTestDonation = this.addWidget(new CheckBox(125, 25, 170, activePanel == PanelsType.Settings, I18n.get("daintegratew.skiptestd"), data.SkipTestDonation, this::SettingsSkipTestDonationClick));
		SETTINGSBUTTON_DTChat = this.addWidget(new CheckBox(125, 65, 80, activePanel == PanelsType.Settings, I18n.get("daintegratew.chat"), data.DonationTo == ChatType.CHAT, this::SettingsDTChatClick));
		SETTINGSBUTTON_DTSystem = this.addWidget(new CheckBox(210, 65, 80, activePanel == PanelsType.Settings, I18n.get("daintegratew.system"), data.DonationTo == ChatType.SYSTEM, this::SettingsDTSystemClick));
		SETTINGSBUTTON_DTGameInfo = this.addWidget(new CheckBox(295, 65, 80, activePanel == PanelsType.Settings, I18n.get("daintegratew.gameinfo"), data.DonationTo == ChatType.GAME_INFO, this::SettingsDTGameClick));
		SETTINGSBUTTON_Save = this.addWidget(new DefaultButton(this.width - 80, 0, 80, activePanel == PanelsType.Settings, I18n.get("daintegratew.save"), this::SettingsSaveClick));
		STATUSBUTTON_Save = this.addWidget(new DefaultButton(125, TOKENPANELY + 45, 120, activePanel == PanelsType.Status, I18n.get("daintegratew.save"), this::StatusSaveClick));
		STATUSBUTTON_Delete = this.addWidget(new DefaultButton(250, TOKENPANELY + 45, 120, activePanel == PanelsType.Status, I18n.get("daintegratew.delete"), this::StatusDeleteClick));
		STATUSBUTTON_ConnectionController = this.addWidget(new DefaultButton(125, 35, 120, activePanel == PanelsType.Status,
				dalets.getConnected() ? I18n.get("daintegratew.disconnect") : I18n.get("daintegratew.connect"), this::ConntectionControllerClick));
		TYPESBUTTON_Save = this.addWidget(new DefaultButton(this.width - 80, 0, 80, activePanel == PanelsType.Types, I18n.get("daintegratew.save"), this::TypesSaveClick));
		TYPESBUTTON_Add = this.addWidget(new CustomButton(this.width - 100, 0, 20, activePanel == PanelsType.Types, "+", this::TypesAddClick));
		TYPESBUTTON_Add.DefaultBackgroundColor = Palette.GREEN;
		TYPESBUTTON_Add.HoveredBackgroundColor = Palette.GREEN_HOVERED;
		TYPESBUTTON_Add.HoveredForegroundColor = Palette.WHITE;
		LEFTBUTTON_SupportAuthor.DefaultBackgroundColor = Palette.BLACK_TRANSPARENT30;
		SETTINGS_ShowCountDonation = new CustomTextBox(fontRenderer, 125, 100, 200, 20, "");
		SETTINGS_ShowCountDonation.tag = I18n.get("daintegratew.countshowdonation");
		SETTINGS_ShowCountDonation.setText(Integer.toString(data.CountDonationInCache));
		text1 = new CustomTextBox(fontRenderer, 125, TOKENPANELY + 10, 200, 20, "");
		text1.setTextColor(Palette.WHITE);
		text1.tag = langToken;

		InitializeMessages();
		InitializeTypes();

		lenghtConnectState = mc.font.width(langConnectState);
		if (activePanel == null)
			this.SetActivePanel(PanelsType.Messages);
		initialized = true;
	}

    private void InitializeMessages()
    {
    	messagesPanel = new ScrollPanel<MessageEntry>(125, 25, this.width, this.height);
		for (int i = data.Donations.size() - 1; i >= 0; i--)
			messagesPanel.addEntry(new MessageEntry(0, 0, this.width - 130, 0, fontRenderer, data.Donations.get(i)));
    }
    
    private void InitializeTypes()
    {
    	typesPanel = new ScrollPanel<DonationTypeEntry>(125, 25, this.width, this.height);
    	for (int i = 0; i < data.TManager.getTypes().size(); i++)
    		typesPanel.addEntry(new DonationTypeEntry(0, 0, 0, 0, typesPanel, data.TManager.getTypes().get(i) , mc, this.width));
    }
    
    private void InitializeLangHelpLines() {
    	langHelpLines = Lists.newArrayList();
    	langHelpLines.add(I18n.get("daintegratew.help.title"));
    	langHelpLines.add(DataCollector.TagDonationMessage + " - " + I18n.get("daintegratew.help.d1"));
    	langHelpLines.add(DataCollector.TagDonationAmount + " - " + I18n.get("daintegratew.help.d2"));
    	langHelpLines.add(DataCollector.TagDonationCurrency + " - " + I18n.get("daintegratew.help.d3"));
    	langHelpLines.add(DataCollector.TagDonationUserName + " - " + I18n.get("daintegratew.help.d4"));
    	langHelpLines.add(DataCollector.TagMinecraftPlayerName + " - " + I18n.get("daintegratew.help.m1"));
    }
    
    //MINECRAFT HANDLERS HERE ------------------------------------------------
    public void tick()
	{
		if (typesSaveTimer > 0)
			typesSaveTimer--;
	}

    public void render(PoseStack matrixs, int mouseX, int mouseY, float partialTicks)
    {
    	if (!initialized)
    		return;
    	//Background
        fill(matrixs,0, 0, this.width, this.height, 0xEE1A1A1E);
        if (activePanel == null)
        	activePanel = PanelsType.Messages;
        //Panels
        switch (activePanel)
    	{
    	case Messages:
    		messagesPanel.drawPanel(matrixs, mouseX, mouseY, partialTicks);
    		break;
    	case Status:
    		drawString(matrixs, fontRenderer, langConnectState, 125, 25, Palette.WHITE);
    		drawString(matrixs, fontRenderer, dalets.getConnected() ? langConnected : langDisonnected, 125 + lenghtConnectState + 5, 25,
    				dalets.getConnected() ? Palette.GREEN : Palette.RED);

			STATUSBUTTON_ConnectionController.drawButton(matrixs, mouseX, mouseY, partialTicks);
			STATUSBUTTON_Save.drawButton(matrixs, mouseX, mouseY, partialTicks);
    		text1.renderButton(matrixs);
    		drawString(matrixs, fontRenderer, data.isTokenExists() ? langTokenExist : langTokenNotExists, 125, TOKENPANELY + 35, data.isTokenExists() ? Palette.GREEN : Palette.RED);
    		break;
    	case Types:
			SETTINGSBUTTON_Save.drawButton(matrixs, mouseX, mouseY, partialTicks);
    		typesPanel.drawPanel(matrixs, mouseX, mouseY, partialTicks);
			TYPESBUTTON_Add.drawButton(matrixs, mouseX, mouseY, partialTicks);
			TYPESBUTTON_Save.drawButton(matrixs, mouseX, mouseY, partialTicks);
			if (typesSuccessfulSave && typesSaveTimer > 0)
    			drawString(matrixs, fontRenderer, langSaved, width - lenghtSaved - 100, 6, Palette.WHITE);
    		else if (!typesSuccessfulSave && typesSaveTimer > 0)
    			drawString(matrixs, fontRenderer, langError, width - lenghtSaved - 100, 6, Palette.RED);
    		break;
    	case Settings:
    		SETTINGS_ShowCountDonation.renderButton(matrixs);
			SETTINGSBUTTON_Save.drawButton(matrixs, mouseX, mouseY, partialTicks);
			SETTINGSBUTTON_DTChat.drawButton(matrixs, mouseX, mouseY, partialTicks);
			SETTINGSBUTTON_DTGameInfo.drawButton(matrixs, mouseX, mouseY, partialTicks);
			SETTINGSBUTTON_DTSystem.drawButton(matrixs, mouseX, mouseY, partialTicks);
			SETTINGSBUTTON_SkippingTestDonation.drawButton(matrixs, mouseX, mouseY, partialTicks);
    		this.drawString(matrixs, fontRenderer, langDonto, 125, 54, Palette.WHITE);
    		break;
    	case Help:
    		for (int i = 0; i < langHelpLines.size(); i++)
    			drawString(matrixs, fontRenderer, langHelpLines.get(i), 125, i * 10 + 25, Palette.WHITE);
    		break;
    	}

		LEFTBUTTON_Messages.drawButton(matrixs, mc, 0, 20, mouseX, mouseY, partialTicks);
		LEFTBUTTON_Status.drawButton(matrixs, mc, 0, 40, mouseX, mouseY, partialTicks);
		LEFTBUTTON_Types.drawButton(matrixs, mc, 0, 60, mouseX, mouseY, partialTicks);
		LEFTBUTTON_Settings.drawButton(matrixs, mc, 0, 80, mouseX, mouseY, partialTicks);
		LEFTBUTTON_Help.drawButton(matrixs, mc, 0, 100, mouseX, mouseY, partialTicks);

        //Left menu
        fill(matrixs, 0, 0, this.width, 20, 0x65000000);
        fill(matrixs, 0, 20, 120, this.height, 0x50000000);
        this.drawString(matrixs, fontRenderer, Main.MODNAME, 5, 5, Palette.WHITE);
        
        super.render(matrixs, mouseX, mouseY, partialTicks);
    }
     
    public boolean charTyped(char typedChar, int keyCode)
    {
    	if (!initialized)
    		return false;
    	if (text1.charTyped(typedChar, keyCode)) {
        }
        else
            super.charTyped(typedChar, keyCode);
    	if (activePanel == PanelsType.Types)
    		typesPanel.charTyped(typedChar, keyCode);
    	if (activePanel == PanelsType.Settings)
    		SETTINGS_ShowCountDonation.charTyped(typedChar, keyCode);
    	return true;
    }

    public boolean keyPressed(int a, int b, int c)
	{
		if (!initialized)
			return false;
		if (activePanel == PanelsType.Types)
			typesPanel.keyPressed(a, b, c);
		if (activePanel == PanelsType.Settings)
            SETTINGS_ShowCountDonation.keyPressed(a, b, c);
		if (activePanel == PanelsType.Status)
		    text1.keyPressed(a, b, c);
		return super.keyPressed(a, b, c);
	}
    
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
		if (!initialized)
			return false;
		super.mouseClicked(mouseX, mouseY, mouseButton);
    	if (activePanel == PanelsType.Status)
    		return text1.mouseClicked(mouseX, mouseY, mouseButton);
    	if (activePanel == PanelsType.Types)
    		return typesPanel.mouseClicked(mouseX, mouseY, mouseButton);
    	if (activePanel == PanelsType.Settings)
    		return SETTINGS_ShowCountDonation.mouseClicked(mouseX, mouseY, mouseButton);
    	return true;
    }
    
    public boolean mouseReleased(double mouseX, double mouseY, int state)
    {
        return super.mouseReleased(mouseX, mouseY, state);
    }

	private void SwitchPanel_Messages(Button button)
	{
		SetActivePanel(PanelsType.Messages);
	}

	private void SwitchPanel_Status(Button button)
	{
		SetActivePanel(PanelsType.Status);
	}

	private void SwitchPanel_Types(Button button)
	{
		SetActivePanel(PanelsType.Types);
	}

	private void SwitchPanel_Settings(Button button)
	{
		SetActivePanel(PanelsType.Settings);
	}

	private void SwitchPanel_Help(Button button)
	{
		SetActivePanel(PanelsType.Help);
	}

	private void SwitchPanel_SupportAuthor(Button button)
	{
		try { Desktop.getDesktop().browse(new URI("https://www.donationalerts.com/r/folleach")); }
		catch (Exception e) { e.printStackTrace(); }
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

	private void SettingsSaveClick(Button button)
	{
		try {
			data.CountDonationInCache = Integer.parseInt(SETTINGS_ShowCountDonation.getText());
			if (data.CountDonationInCache < 1)
				data.CountDonationInCache = 1;
			data.Save();
			data.RecountDonationCache();
			SETTINGS_ShowCountDonation.LineColor = Palette.GRAY30_TRANSPARENT_xDD;
			SETTINGS_ShowCountDonation.setText(Integer.toString(data.CountDonationInCache));
		} catch (NumberFormatException e) {
			SETTINGS_ShowCountDonation.LineColor = Palette.RED;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void StatusSaveClick(Button button)
	{
		data.Token = text1.getText();
		try {
			data.Save();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	private void StatusDeleteClick(Button button)
	{
		data.Token = "";
		try {
			data.Save();
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	private void ConntectionControllerClick(Button button)
	{
		if (dalets.getConnected()) {
			dalets.Disconnect();
			STATUSBUTTON_ConnectionController.setMessage(new TextComponent(I18n.get("daintegratew.connect")));
		}
		else {
			try {
				dalets.Connect(data.Token);
			} catch (JSONException e) {
				Main.DonationAlertsInformation(I18n.get("daintegratew.error"));
				e.printStackTrace();
			}
			STATUSBUTTON_ConnectionController.setMessage(new TextComponent(I18n.get("daintegratew.disconnect")));
		}
	}

	private void TypesSaveClick(Button button) {
		TypesManager tManager = new TypesManager();
		DonationType temp;
		List<DonationTypeEntry> entries = typesPanel.getEntries();
		try
		{
			for (int i = 0; i < entries.size(); i++)
			{
				if (entries.get(i).hasError)
				{
					typesSaveTimer = 120;
					typesSuccessfulSave = false;
					return;
				}
				temp = new DonationType();
				temp.Name = entries.get(i).getName();
				temp.Active = entries.get(i).getActive();
				temp.CurrencyBRL = getCurrency(entries.get(i).CurrencyBRL.getText());
				temp.CurrencyBYN = getCurrency(entries.get(i).CurrencyBYN.getText());
				temp.CurrencyEUR = getCurrency(entries.get(i).CurrencyEUR.getText());
				temp.CurrencyKZT = getCurrency(entries.get(i).CurrencyKZT.getText());
				temp.CurrencyRUB = getCurrency(entries.get(i).CurrencyRUB.getText());
				temp.CurrencyUAH = getCurrency(entries.get(i).CurrencyUAH.getText());
				temp.CurrencyUSD = getCurrency(entries.get(i).CurrencyUSD.getText());
				temp.setMessages(entries.get(i).getMessages());
				temp.setCommands(entries.get(i).getCommands());
				tManager.getTypes().add(temp);
			}
			data.TManager = tManager;
			data.Save();
			typesSaveTimer = 120;
			typesSuccessfulSave = true;
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	private float getCurrency(String text) {
		try {
			return Float.parseFloat(text);
		}
		catch (NumberFormatException e) {
			return 0;
		}
	}

	private void TypesAddClick(Button button)
	{
		typesPanel.addEntry(new DonationTypeEntry(0, 0, 0, 0, typesPanel, new DonationType(), mc, this.width));
	}
    
    public void onGuiClosed() {
        // Keyboard.enableRepeatEvents(false);
    }

	public boolean mouseScrolled(double a, double b, double delta)
	{
		if (activePanel == PanelsType.Messages)
        	messagesPanel.mouseScrolled(a, b, delta);
        if (activePanel == PanelsType.Types)
        	typesPanel.mouseScrolled(a, b, delta);
        return true;
	}

//    public void handleMouseInput() throws IOException
//    {
//        handleMouseInput();
//        if (activePanel == PanelsType.Messages)
//        	messagesPanel.handleMouseInput();
//        if (activePanel == PanelsType.Types)
//        	typesPanel.handleMouseInput();
//    }
    //END MINECRAFT HANDLERS =================================================
    
    void setVisibilitySettingsPanel(boolean value) {
    	SETTINGSBUTTON_SkippingTestDonation.visible =
    		SETTINGSBUTTON_DTChat.visible =
    		SETTINGSBUTTON_DTSystem.visible =
    		SETTINGSBUTTON_DTGameInfo.visible =
    		SETTINGSBUTTON_Save.visible =
    		value;
    }
    
    void setVisibilityStatusPanel(boolean value) {
    	STATUSBUTTON_Save.visible =
    		STATUSBUTTON_Delete.visible =
    		STATUSBUTTON_ConnectionController.visible =
    		//STATUSLABEL_Warning.visible =
    		value;
    }
    
    void setVisibilityMessagesPanel(boolean value) {
    	messagesPanel.visible = value;
    }
    
    void setVisibilityTypesPanel(boolean value) {
    	TYPESBUTTON_Save.visible =
    		TYPESBUTTON_Add.visible = value;
    }
    
    public void SetActivePanel(PanelsType t) {
    	if (activePanel == t)
    		return;
    	
    	setVisibilitySettingsPanel(false);
		setVisibilityStatusPanel(false);
		setVisibilityMessagesPanel(false);
		setVisibilityTypesPanel(false);
    	switch (t)
    	{
    	case Messages: setVisibilityMessagesPanel(true); break;
    	case Status: setVisibilityStatusPanel(true); break;
    	case Types: setVisibilityTypesPanel(true); break;
    	case Settings: setVisibilitySettingsPanel(true); break;
    	case Help: if (langHelpLines == null) InitializeLangHelpLines(); break;
    	}
    	activePanel = t;
    }
    
    public boolean doesGuiPauseGame() {
        return false;
    }
}
