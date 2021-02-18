package com.folleach.daintegrate;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.folleach.gui.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.json.JSONException;

import com.folleach.donationalerts.DonationAlerts;
import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ChatType;
import org.json.JSONObject;

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
	private FontRenderer fontRenderer;
	private DataCollector data;
	private DonationAlerts donationAlerts;
	private DonationAlertsIntegrate donationAlertsIntegrate;
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

	public MainWindow(Minecraft mc,
					  DataCollector data,
					  DonationAlerts donationAlerts,
					  DonationAlertsIntegrate donationAlertsIntegrate)
	{
		super(new StringTextComponent("MainWindow"));
		this.mc = mc;
		fontRenderer = mc.fontRenderer;
		this.data = data;
		this.donationAlerts = donationAlerts;
		this.donationAlertsIntegrate = donationAlertsIntegrate;
		InitializeStrings();
		
	}
	
	private void InitializeStrings()
	{
		langDonto = I18n.format("daintegratew.donto")+":";
		langToken = I18n.format("daintegratew.token")+":";
		langTokenExist = I18n.format("daintegratew.token.exists")+".";
		langTokenNotExists = I18n.format("daintegratew.token.absents")+".";
		langConnected = I18n.format("daintegratew.connected")+".";
		langDisonnected = I18n.format("daintegratew.disconnected")+".";
		langConnectState = I18n.format("daintegratew.connstatus")+":";
		langSaved = I18n.format("daintegratew.saved");
		langError = I18n.format("daintegratew.error");
		lenghtSaved = mc.fontRenderer.getStringWidth(langSaved) + 10;
	}

    public void init() {
		initialized = false;
		this.buttons.clear();
		this.children.clear();
		LEFTBUTTON_Messages = this.addButton(new CustomButton(0, 20, 120, 20, I18n.format("daintegratew.messages"), this::SwitchPanel_Messages));
		LEFTBUTTON_Status = this.addButton(new CustomButton(0, 20 + SPACE_LEFTBUTTON, 120, 20, I18n.format("daintegratew.status"), this::SwitchPanel_Status));
		LEFTBUTTON_Types = this.addButton(new CustomButton(0, 20 + (2 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.types"), this::SwitchPanel_Types));
		LEFTBUTTON_Settings = this.addButton(new CustomButton(0, 20 + (3 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.settings"), this::SwitchPanel_Settings));
		LEFTBUTTON_Help = this.addButton(new CustomButton(0, 20 + (4 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.help"), this::SwitchPanel_Help));
		LEFTBUTTON_SupportAuthor = this.addButton(new DefaultButton(0, height - 20, 120, 20, I18n.format("daintegratew.suppauthor"), this::SwitchPanel_SupportAuthor));
		SETTINGSBUTTON_SkippingTestDonation = this.addButton(new CheckBox(125, 25, 170, activePanel == PanelsType.Settings, I18n.format("daintegratew.skiptestd"), data.SkipTestDonation, this::SettingsSkipTestDonationClick));
		SETTINGSBUTTON_DTChat = this.addButton(new CheckBox(125, 65, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.chat"), data.DonationTo == ChatType.CHAT, this::SettingsDTChatClick));
		SETTINGSBUTTON_DTSystem = this.addButton(new CheckBox(210, 65, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.system"), data.DonationTo == ChatType.SYSTEM, this::SettingsDTSystemClick));
		SETTINGSBUTTON_DTGameInfo = this.addButton(new CheckBox(295, 65, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.gameinfo"), data.DonationTo == ChatType.GAME_INFO, this::SettingsDTGameClick));
		SETTINGSBUTTON_Save = this.addButton(new DefaultButton(this.width - 80, 0, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.save"), this::SettingsSaveClick));
		STATUSBUTTON_Save = this.addButton(new DefaultButton(125, TOKENPANELY + 45, 120, activePanel == PanelsType.Status, I18n.format("daintegratew.save"), this::StatusSaveClick));
		STATUSBUTTON_Delete = this.addButton(new DefaultButton(250, TOKENPANELY + 45, 120, activePanel == PanelsType.Status, I18n.format("daintegratew.delete"), this::StatusDeleteClick));
		STATUSBUTTON_ConnectionController = this.addButton(new DefaultButton(125, 35, 120, activePanel == PanelsType.Status,
				donationAlerts.getConnected() ? I18n.format("daintegratew.disconnect") : I18n.format("daintegratew.connect"), this::ConntectionControllerClick));
		TYPESBUTTON_Save = this.addButton(new DefaultButton(this.width - 80, 0, 80, activePanel == PanelsType.Types, I18n.format("daintegratew.save"), this::TypesSaveClick));
		TYPESBUTTON_Add = this.addButton(new CustomButton(this.width - 100, 0, 20, activePanel == PanelsType.Types, "+", this::TypesAddClick));
		TYPESBUTTON_Add.DefaultBackgroundColor = Palette.GREEN;
		TYPESBUTTON_Add.HoveredBackgroundColor = Palette.GREEN_HOVERED;
		TYPESBUTTON_Add.HoveredForegroundColor = Palette.WHITE;
		LEFTBUTTON_SupportAuthor.DefaultBackgroundColor = Palette.BLACK_TRANSPARENT30;
		SETTINGS_ShowCountDonation = new CustomTextBox(fontRenderer, 125, 90, 200, 20, "");
		SETTINGS_ShowCountDonation.tag = I18n.format("daintegratew.countshowdonation");
		SETTINGS_ShowCountDonation.setText(Integer.toString(data.CountDonationInCache));
		text1 = new CustomTextBox(fontRenderer, 125, TOKENPANELY, 200, 20, "");
		text1.setTextColor(Palette.WHITE);
		text1.tag = langToken;

		InitializeMessages();
		InitializeTypes();

		lenghtConnectState = mc.fontRenderer.getStringWidth(langConnectState);
		if (activePanel == null)
			this.SetActivePanel(PanelsType.Messages);
		initialized = true;
	}

    private void InitializeMessages()
    {
    	messagesPanel = new ScrollPanel<MessageEntry>(125, 25, this.width, this.height);
		for (int i = donationAlertsIntegrate.Donations.size() - 1; i >= 0; i--)
			messagesPanel.addEntry(new MessageEntry(0, 0, this.width - 130, 0, fontRenderer, donationAlertsIntegrate.Donations.get(i)));
    }
    
    private void InitializeTypes()
    {
    	typesPanel = new ScrollPanel<DonationTypeEntry>(125, 25, this.width, this.height);
    	for (int i = 0; i < data.typesManager.getTypes().size(); i++)
    		typesPanel.addEntry(new DonationTypeEntry(0, 0, 0, 0, typesPanel, data.typesManager.getTypes().get(i) , mc, this.width));
    }
    
    private void InitializeLangHelpLines() {
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
    
    //MINECRAFT HANDLERS HERE ------------------------------------------------
    public void tick()
	{
		if (typesSaveTimer > 0)
			typesSaveTimer--;
	}

    public void render(MatrixStack matrixs, int mouseX, int mouseY, float partialTicks)
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
    		drawString(matrixs, fontRenderer, donationAlerts.getConnected() ? langConnected : langDisonnected, 125 + lenghtConnectState + 5, 25,
    				donationAlerts.getConnected() ? Palette.GREEN : Palette.RED);
    		
    		text1.renderButton(matrixs);
    		drawString(matrixs, fontRenderer, data.isTokenExists() ? langTokenExist : langTokenNotExists, 125, TOKENPANELY + 35, data.isTokenExists() ? Palette.GREEN : Palette.RED);
    		break;
    	case Types:
    		typesPanel.drawPanel(matrixs, mouseX, mouseY, partialTicks);
    		if (typesSuccessfulSave && typesSaveTimer > 0)
    			drawString(matrixs, fontRenderer, langSaved, width - lenghtSaved - 100, 6, Palette.WHITE);
    		else if (!typesSuccessfulSave && typesSaveTimer > 0)
    			drawString(matrixs, fontRenderer, langError, width - lenghtSaved - 100, 6, Palette.RED);
    		break;
    	case Settings:
    		SETTINGS_ShowCountDonation.renderButton(matrixs);
    		this.drawString(matrixs, fontRenderer, langDonto, 125, 54, Palette.WHITE);
    		break;
    	case Help:
    		for (int i = 0; i < langHelpLines.size(); i++)
    			drawString(matrixs, fontRenderer, langHelpLines.get(i), 125, i * 10 + 25, Palette.WHITE);
    		break;
    	}
        
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
			donationAlertsIntegrate.RecountDonationCache();
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
				temp.CurrencyBRL = (float) Double.parseDouble(entries.get(i).CurrencyBRL.getText());
				temp.CurrencyBYN = (float) Double.parseDouble(entries.get(i).CurrencyBYN.getText());
				temp.CurrencyEUR = (float) Double.parseDouble(entries.get(i).CurrencyEUR.getText());
				temp.CurrencyKZT = (float) Double.parseDouble(entries.get(i).CurrencyKZT.getText());
				temp.CurrencyRUB = (float) Double.parseDouble(entries.get(i).CurrencyRUB.getText());
				temp.CurrencyUAH = (float) Double.parseDouble(entries.get(i).CurrencyUAH.getText());
				temp.CurrencyUSD = (float) Double.parseDouble(entries.get(i).CurrencyUSD.getText());
				if (entries != null)
				for (String value : entries.get(i).getMessages()) {
					temp.addAction(new Action(
							"message",
							0,
							new JSONObject().put("message", value)));
				}
				if (entries != null)
				for (String value : entries.get(i).getCommands()) {
					temp.addAction(new Action(
							"command",
							0,
							new JSONObject().put("command", value)));
				}
				tManager.getTypes().add(temp);
			}
			data.typesManager = tManager;
			data.Save();
			typesSaveTimer = 120;
			typesSuccessfulSave = true;
		} catch (JSONException | IOException e) {
			e.printStackTrace();
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
