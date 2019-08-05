package com.folleach.daintegrate;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.json.JSONException;
import org.lwjgl.input.Keyboard;

import com.folleach.donationalerts.DonationAlerts;
import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;
import com.folleach.gui.CheckBox;
import com.folleach.gui.CustomButton;
import com.folleach.gui.CustomTextBox;
import com.folleach.gui.DefaultButton;
import com.folleach.gui.DonationTypeEntry;
import com.folleach.gui.MessageEntry;
import com.folleach.gui.ScrollPanel;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ChatType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MainWindow extends GuiScreen
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
	private GuiLabel STATUSLABEL_Warning;
	
	private DefaultButton TYPESBUTTON_Save;
	private CustomButton TYPESBUTTON_Add;
	
	private CheckBox SETTINGSBUTTON_SkippingTestDonation;
	private CheckBox SETTINGSBUTTON_DTChat;
	private CheckBox SETTINGSBUTTON_DTSystem;
	private CheckBox SETTINGSBUTTON_DTGameInfo;
	private DefaultButton SETTINGSBUTTON_Save;
	
	private CustomTextBox text1;
	
	private ScrollPanel<MessageEntry> messagesPanel;
	private ScrollPanel<DonationTypeEntry> typesPanel;
	
	private boolean typesSuccessfulSave;
	private int typesSaveTimer;
	
	public MainWindow(Minecraft mc, DataCollector d, DonationAlerts da)
	{
		this.mc = mc;
		data = d;
		dalets = da;
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
	
    public void initGui()
    {
        this.buttonList.clear();
        this.labelList.clear();
        this.buttonList.add(LEFTBUTTON_Messages = new CustomButton(0, 0, 20 + (0 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.messages")));
        this.buttonList.add(LEFTBUTTON_Status = new CustomButton(1, 0, 20 + (1 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.status")));
        this.buttonList.add(LEFTBUTTON_Types = new CustomButton(2, 0, 20 + (2 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.types")));
        this.buttonList.add(LEFTBUTTON_Settings = new CustomButton(3, 0, 20 + (3 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.settings")));
        this.buttonList.add(LEFTBUTTON_Settings = new CustomButton(20, 0, 20 + (4 * SPACE_LEFTBUTTON), 120, 20, I18n.format("daintegratew.help")));
        this.buttonList.add(LEFTBUTTON_SupportAuthor = new DefaultButton(4, 0, height - 20, 120, 20, I18n.format("daintegratew.suppauthor")));
        this.buttonList.add(SETTINGSBUTTON_SkippingTestDonation = new CheckBox(5, 125, 25, 170, activePanel == PanelsType.Settings, I18n.format("daintegratew.skiptestd"), data.SkipTestDonation));
        this.buttonList.add(SETTINGSBUTTON_DTChat = new CheckBox(6, 125, 65, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.chat"), data.DonationTo == ChatType.CHAT));
        this.buttonList.add(SETTINGSBUTTON_DTSystem = new CheckBox(7, 210, 65, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.system"), data.DonationTo == ChatType.SYSTEM));
        this.buttonList.add(SETTINGSBUTTON_DTGameInfo = new CheckBox(8, 295, 65, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.gameinfo"), data.DonationTo == ChatType.GAME_INFO));
        this.buttonList.add(SETTINGSBUTTON_Save = new DefaultButton(9, this.width - 80, 0, 80, activePanel == PanelsType.Settings, I18n.format("daintegratew.save")));
        this.buttonList.add(STATUSBUTTON_Save = new DefaultButton(10, 125, TOKENPANELY + 45, 120, activePanel == PanelsType.Status, I18n.format("daintegratew.save")));
        this.buttonList.add(STATUSBUTTON_Delete = new DefaultButton(11, 250, TOKENPANELY + 45, 120, activePanel == PanelsType.Status, I18n.format("daintegratew.delete")));
        this.buttonList.add(STATUSBUTTON_ConnectionController = new DefaultButton(12, 125, 35, 120, activePanel == PanelsType.Status,
        		dalets.getConnected() ? I18n.format("daintegratew.disconnect") : I18n.format("daintegratew.connect")));
        this.buttonList.add(TYPESBUTTON_Save = new DefaultButton(13, this.width - 80, 0, 80, activePanel == PanelsType.Types, I18n.format("daintegratew.save")));
        this.buttonList.add(TYPESBUTTON_Add = new CustomButton(14, this.width - 100, 0, 20, activePanel == PanelsType.Types, "+"));
        TYPESBUTTON_Add.DefaultBackgroundColor = Pallete.GREEN;
        TYPESBUTTON_Add.HoveredBackgroundColor = Pallete.GREEN_HOVERED;
        TYPESBUTTON_Add.HoveredForegroundColor = Pallete.WHITE;
        LEFTBUTTON_SupportAuthor.DefaultBackgroundColor = Pallete.BLACK_TRANSPERIENT30;
        this.labelList.add(STATUSLABEL_Warning = new GuiLabel(fontRenderer, 201, 125, 130, width - 130, 30, Pallete.WHITE));
        STATUSLABEL_Warning.visible = activePanel == PanelsType.Status;
        STATUSLABEL_Warning.addLine("daintegratew.warning.line1");
        STATUSLABEL_Warning.addLine("daintegratew.warning.line2");
        STATUSLABEL_Warning.addLine("daintegratew.warning.line3");
        Keyboard.enableRepeatEvents(true);
        text1 = new CustomTextBox(100, fontRenderer, 125, TOKENPANELY, 200, 20);
        text1.setTextColor(Pallete.WHITE);
        text1.tag = langToken;
		
        InitializeMessages();
        InitializeTypes();
        
        lenghtConnectState = mc.fontRenderer.getStringWidth(langConnectState);
        if (activePanel == null)
        	this.SetActivePanel(PanelsType.Messages);
    }
    
    private void InitializeMessages()
    {
    	messagesPanel = new ScrollPanel<MessageEntry>(125, 25, this.width, this.height);
		for (int i = data.Donations.size() - 1; i >= 0; i--)
			messagesPanel.addEntry(new MessageEntry(mc.fontRenderer, data.Donations.get(i), this.width - 130));
    }
    
    private void InitializeTypes()
    {
    	typesPanel = new ScrollPanel<DonationTypeEntry>(125, 25, this.width, this.height);
    	for (int i = 0; i < data.TManager.getTypes().size(); i++)
    		typesPanel.addEntry(new DonationTypeEntry(typesPanel, data.TManager.getTypes().get(i) , mc, this.width));
    }
    
    private void InitializeLangHelpLines() {
    	langHelpLines = Lists.<String>newArrayList();
    	langHelpLines.add(I18n.format("daintegratew.help.title"));
    	langHelpLines.add(DataCollector.TagDonationMessage + " - " + I18n.format("daintegratew.help.d1"));
    	langHelpLines.add(DataCollector.TagDonationAmount + " - " + I18n.format("daintegratew.help.d2"));
    	langHelpLines.add(DataCollector.TagDonationCurrency + " - " + I18n.format("daintegratew.help.d3"));
    	langHelpLines.add(DataCollector.TagDonationUserName + " - " + I18n.format("daintegratew.help.d4"));
    	langHelpLines.add(DataCollector.TagMinecraftPlayerName + " - " + I18n.format("daintegratew.help.m1"));
    }
    
    //MINECRAFT HANDLERS HERE ------------------------------------------------
    public void updateScreen()
    {
    	if (typesSaveTimer > 0)
        	typesSaveTimer--;
    }
    
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
    	//Background
        this.drawRect(0, 0, this.width, this.height, 0xEE1A1A1E);
        
        //Panels
        switch (activePanel)
    	{
    	case Messages:
    		messagesPanel.drawPanel(mouseX, mouseY, partialTicks);
    		break;
    	case Status:
    		drawString(fontRenderer, langConnectState, 125, 25, Pallete.WHITE);
    		drawString(fontRenderer, dalets.getConnected() ? langConnected : langDisonnected, 125 + lenghtConnectState + 5, 25,
    				dalets.getConnected() ? Pallete.GREEN : Pallete.RED);
    		
    		text1.drawTextBox();
    		drawString(fontRenderer, data.isTokenExists() ? langTokenExist : langTokenNotExists, 125, TOKENPANELY + 35, data.isTokenExists() ? Pallete.GREEN : Pallete.RED);
    		break;
    	case Types:
    		typesPanel.drawPanel(mouseX, mouseY, partialTicks);
    		if (typesSuccessfulSave && typesSaveTimer > 0)
    			drawString(fontRenderer, langSaved, width - lenghtSaved - 100, 6, Pallete.WHITE);
    		else if (!typesSuccessfulSave && typesSaveTimer > 0)
    			drawString(fontRenderer, langError, width - lenghtSaved - 100, 6, Pallete.RED);
    		break;
    	case Settings:
    		this.drawString(fontRenderer, langDonto, 125, 54, Pallete.WHITE);
    		break;
    	case Help:
    		for (int i = 0; i < langHelpLines.size(); i++)
    			drawString(fontRenderer, langHelpLines.get(i), 125, i * 10 + 25, Pallete.WHITE);
    		break;
    	}
        
        //Left menu
        this.drawRect(0, 0, this.width, 20, 0x65000000);
        this.drawRect(0, 20, 120, this.height, 0x50000000);
        this.drawString(fontRenderer, Main.MODNAME, 5, 5, Pallete.WHITE);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
     
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    	if (text1.textboxKeyTyped(typedChar, keyCode)) {	
        }
        else
            super.keyTyped(typedChar, keyCode);
    	if (activePanel == activePanel.Types)
    		typesPanel.keyTyped(typedChar, keyCode);
    }
    
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
    	super.mouseClicked(mouseX, mouseY, mouseButton);
    	if (activePanel == PanelsType.Status)
    		text1.mouseClicked(mouseX, mouseY, mouseButton);
    	if (activePanel == PanelsType.Types)
    		typesPanel.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    protected void actionPerformed(GuiButton button)
    {
    	CheckBox chb;

    	switch (button.id)
    	{
    	case 0: SetActivePanel(PanelsType.Messages); break;
    	case 1: SetActivePanel(PanelsType.Status); break;
    	case 2: SetActivePanel(PanelsType.Types); break;
    	case 3: SetActivePanel(PanelsType.Settings); break;
    	case 4:
    		try { Desktop.getDesktop().browse(new URI("https://www.donationalerts.com/r/folleach")); }
    		catch (Exception e) { e.printStackTrace(); }
    		break;
    	case 5:
    		chb = (CheckBox)button;
    		chb.SwitchFlag(!chb.Flag);
    		data.SkipTestDonation = (chb.Flag);
    		break;
    	case 6:
    		SETTINGSBUTTON_DTSystem.SwitchFlag(false);
    		SETTINGSBUTTON_DTGameInfo.SwitchFlag(false);
    		chb = (CheckBox)button;
    		chb.SwitchFlag(true);
    		data.DonationTo = ChatType.CHAT;
    		break;
		case 7:
			SETTINGSBUTTON_DTChat.SwitchFlag(false);
    		SETTINGSBUTTON_DTGameInfo.SwitchFlag(false);
			chb = (CheckBox)button;
    		chb.SwitchFlag(true);
    		data.DonationTo = ChatType.SYSTEM;
			break;
		case 8:
			SETTINGSBUTTON_DTChat.SwitchFlag(false);
    		SETTINGSBUTTON_DTSystem.SwitchFlag(false);
			chb = (CheckBox)button;
    		chb.SwitchFlag(true);
    		data.DonationTo = ChatType.GAME_INFO;
			break;
		case 9:
			try {
				data.Save();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 10:
			data.FEmptyString = text1.getText();
			try {
				data.Save();
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
			break;
		case 11:
			data.FEmptyString = "";
			try {
				data.Save();
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
			break;
		case 12:
			if (dalets.getConnected()) {
				dalets.Disconnect();
				STATUSBUTTON_ConnectionController.displayString = I18n.format("daintegratew.connect");
			}
			else {
				try {
					dalets.Connect(data.FEmptyString);
				} catch (JSONException e) {
					Main.DonationAlertsInformation(I18n.format("daintegratew.error"));
					e.printStackTrace();
				}
				STATUSBUTTON_ConnectionController.displayString = I18n.format("daintegratew.disconnect");
			}
			break;
		case 13:
			TypesManager tManager = new TypesManager();
			DonationType temp;
			List<DonationTypeEntry> entries = typesPanel.getEntries();
			for (int i = 0; i < entries.size(); i++) {
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
				temp.setMessages(entries.get(i).getMessages());
				temp.setCommands(entries.get(i).getCommands());
				tManager.getTypes().add(temp);
			}
			data.TManager = tManager;
			try {
				data.Save();
				typesSaveTimer = 120;
				typesSuccessfulSave = true;
			}
			catch (JSONException | IOException  e) { e.printStackTrace(); }
			break;
		case 14:
			typesPanel.addEntry(new DonationTypeEntry(typesPanel, new DonationType(), mc, this.width));
			break;
		case 20:
			SetActivePanel(PanelsType.Help);
			break;
    	}
    }
    
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        if (activePanel == PanelsType.Messages)
        	messagesPanel.handleMouseInput();
        if (activePanel == PanelsType.Types)
        	typesPanel.handleMouseInput();
    }
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
    		STATUSLABEL_Warning.visible =
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
