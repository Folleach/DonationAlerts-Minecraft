package com.folleach.gui;

import java.util.Iterator;
import java.util.List;

import com.folleach.daintegrate.Pallete;
import com.folleach.donationalerts.DonationType;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Int;

@SideOnly(Side.CLIENT)
public class DonationTypeEntry extends Gui implements IEntry {
	private static final String langMessages;
	private static final String langCommands;
	private static final String langLine;
	
	static {
		langMessages = I18n.format("daintegratew.messages");
		langCommands = I18n.format("daintegratew.commands");
		langLine = I18n.format("daintegratew.line");
	}
	
	private final ScrollPanel owner;
	public final DonationType type;
	private final FontRenderer fontRenderer;
	private final Minecraft mc;
	private int right;
	
	private CustomTextBox textBoxName;
	private CheckBox checkBoxActive;
	
	private List<WritableLineElement> messages;
	private List<WritableLineElement> commands;
	private CustomButton addMessage;
	private CustomButton addCommand;
	private CustomButton deleteEntry;
	
	public CustomTextBox CurrencyBRL;
	public CustomTextBox CurrencyBYN;
	public CustomTextBox CurrencyEUR;
	public CustomTextBox CurrencyKZT;
	public CustomTextBox CurrencyRUB;
	public CustomTextBox CurrencyUAH;
	public CustomTextBox CurrencyUSD;
	
	public boolean hasError = false;
	
	public DonationTypeEntry(ScrollPanel own, DonationType type, Minecraft mc, int right)
	{
		this.right = right;
		owner = own;
		messages = Lists.<WritableLineElement>newArrayList();
		commands = Lists.<WritableLineElement>newArrayList();
		
		addMessage = new CustomButton(2002, 0, 0, 20, true, "+");
		addMessage.DefaultBackgroundColor = Pallete.GREEN;
		addMessage.HoveredBackgroundColor = Pallete.GREEN_HOVERED;
		addMessage.HoveredForegroundColor = Pallete.WHITE;
		addCommand = new CustomButton(2002, 0, 0, 20, true, "+");
		addCommand.DefaultBackgroundColor = Pallete.GREEN;
		addCommand.HoveredBackgroundColor = Pallete.GREEN_HOVERED;
		addCommand.HoveredForegroundColor = Pallete.WHITE;
		
		this.type = type;
		this.fontRenderer = mc.fontRenderer;
		this.mc = mc;
		textBoxName = new CustomTextBox(1, fontRenderer, 0, 0, 200, 20);
		textBoxName.tag = I18n.format("daintegratew.name");
		textBoxName.setText(type.Name);
		checkBoxActive = new CheckBox(8001, 0, 0, 60, true, I18n.format("daintegratew.active"), type.Active);
		
		deleteEntry = new CustomButton(0, 0, 0, 20, true, "-");
		deleteEntry.DefaultBackgroundColor = Pallete.RED;
		deleteEntry.HoveredBackgroundColor = Pallete.RED_HOVERED;
		deleteEntry.HoveredForegroundColor = Pallete.WHITE;
		WritableLineElement temp;
		if (type.getMessages() != null)
		for (int i = 0; i < type.getMessages().size(); i++) {
			temp = new WritableLineElement(this, mc, WritableType.Message, langLine + " " + (i + 1));
			temp.line.setText(type.getMessages().get(i));
			messages.add(temp);
			
		}
		if (type.getCommands() != null)
		for (int i = 0; i < type.getCommands().size(); i++) {
			temp = new WritableLineElement(this, mc, WritableType.Command, langLine + " " + (i + 1));
			temp.line.setText(type.getCommands().get(i));
			commands.add(temp);
			
		}
		
		CurrencyBRL = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyBRL.tag = "BRL";
		CurrencyBRL.setText(String.valueOf(type.CurrencyBRL));
		CurrencyBYN = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyBYN.tag = "BYN";
		CurrencyBYN.setText(String.valueOf(type.CurrencyBYN));
		CurrencyEUR = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyEUR.tag = "EUR";
		CurrencyEUR.setText(String.valueOf(type.CurrencyEUR));
		CurrencyKZT = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyKZT.tag = "KZT";
		CurrencyKZT.setText(String.valueOf(type.CurrencyKZT));
		CurrencyRUB = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyRUB.tag = "RUB";
		CurrencyRUB.setText(String.valueOf(type.CurrencyRUB));
		CurrencyUAH = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyUAH.tag = "UAH";
		CurrencyUAH.setText(String.valueOf(type.CurrencyUAH));
		CurrencyUSD = new CustomTextBox(0, fontRenderer, 0, 0, 80, 20);
		CurrencyUSD.tag = "USD";
		CurrencyUSD.setText(String.valueOf(type.CurrencyUSD));
	}
	
	@Override
	public void drawEntry(int x, int y, int mouseX, int mouseY, float partialTicks) {
		int offset = y;
		textBoxName.drawTextBox(x, offset);
		checkBoxActive.drawButton(mc, x + 210, y + 10, mouseX, mouseY, partialTicks);
		deleteEntry.drawButton(mc, x + 275, y + 10, mouseX, mouseY, partialTicks);
		offset += 35;
		fontRenderer.drawString(langMessages, x, offset, Pallete.WHITE);
		offset += 10;
		for (int i = 0; i < messages.size(); i++)
		{
			messages.get(i).drawElement(x, offset, mouseX, mouseY, partialTicks);
			offset += 35;
		}
		addMessage.drawButton(mc, x + 210, offset, mouseX, mouseY, partialTicks);
		offset += 25;
		fontRenderer.drawString(langCommands, x, offset, Pallete.WHITE);
		offset += 10;
		for (int i = 0; i < commands.size(); i++)
		{
			commands.get(i).drawElement(x, offset, mouseX, mouseY, partialTicks);
			offset += 35;
		}
		addCommand.drawButton(mc, x + 210, offset, mouseX, mouseY, partialTicks);
		offset += 25;
		CurrencyUSD.drawTextBox(x, offset); CurrencyRUB.drawTextBox(x + 90, offset);
		offset += 35;
		CurrencyEUR.drawTextBox(x, offset); CurrencyKZT.drawTextBox(x + 90, offset);
		offset += 35;
		CurrencyBRL.drawTextBox(x, offset); CurrencyBYN.drawTextBox(x + 90, offset);
		offset += 35;
		CurrencyUAH.drawTextBox(x, offset);
		offset += 35;
		drawRect(x, offset, right - 10, offset + 1, Pallete.GRAY60);
		offset += 20;
	}

	@Override
	public int getHeight() {
		
		return 265 + (messages.size() * 35) + (commands.size() * 35);
	}
	
	public void removeMessage(WritableLineElement ctb) {
		messages.remove(ctb);
		for (int i = 0; i < messages.size(); i++)
			messages.get(i).changeTag(langLine + " " + (i + 1));
	}

	public void removeCommands(WritableLineElement ctb) {
		commands.remove(ctb);
		for (int i = 0; i < commands.size(); i++)
			commands.get(i).changeTag(langLine + " " + (i + 1));
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		for (WritableLineElement element : messages)
			element.keyTyped(typedChar, keyCode);
		for (WritableLineElement element : commands)
			element.keyTyped(typedChar, keyCode);
		
		textBoxName.textboxKeyTyped(typedChar, keyCode);
		if (CurrencyBRL.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyBRL);
		if (CurrencyBYN.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyBYN);
		if (CurrencyEUR.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyEUR);
		if (CurrencyKZT.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyKZT);
		if (CurrencyRUB.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyRUB);
		if (CurrencyUAH.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyUAH);
		if (CurrencyUSD.textboxKeyTyped(typedChar, keyCode))
			checkCurrency(CurrencyUSD);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		for (int i = 0; i < messages.size(); i++)
			messages.get(i).mouseClicked(mouseX, mouseY, mouseButton);

		for (int i = 0; i < commands.size(); i++)
			commands.get(i).mouseClicked(mouseX, mouseY, mouseButton);
		textBoxName.mouseClicked(mouseX, mouseY, mouseButton);
		
		if (checkBoxActive.isClick(mouseX, mouseY)) {
			checkBoxActive.SwitchFlag(!checkBoxActive.Flag);
			checkBoxActive.playPressSound(mc.getSoundHandler());
		}
		if (addMessage.isClick(mouseX, mouseY)) {
			messages.add(new WritableLineElement(this, mc, WritableType.Message, langLine + " " + (messages.size() + 1)));
			addMessage.playPressSound(mc.getSoundHandler());
		}
		if (addCommand.isClick(mouseX, mouseY)) {
			commands.add(new WritableLineElement(this, mc, WritableType.Command, langLine + " " + (commands.size() + 1)));
			addCommand.playPressSound(mc.getSoundHandler());
		}
		if (deleteEntry.isClick(mouseX, mouseY)) {
			owner.removeEntry(this);
			deleteEntry.playPressSound(mc.getSoundHandler());
		}
		
		if (CurrencyBRL.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyBRL);
		if (CurrencyBYN.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyBYN);
		if (CurrencyEUR.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyEUR);
		if (CurrencyKZT.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyKZT);
		if (CurrencyRUB.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyRUB);
		if (CurrencyUAH.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyUAH);
		if (CurrencyUSD.mouseClicked(mouseX, mouseY, mouseButton))
			checkCurrency(CurrencyUSD);
	}
	
	private void checkCurrency(CustomTextBox textBox) {
		try {
			Double.parseDouble(textBox.getText());
			textBox.LineColor = Pallete.GRAY30_TRANSPERIENTDD;
			hasError = false;
		}
		catch (Exception e) {
			textBox.LineColor = Pallete.RED;
			hasError = true;
		}
	}
	
	public String getName() {
		return textBoxName.getText();
	}
	
	public boolean getActive() {
		return checkBoxActive.Flag;
	}
	
	public List<String> getMessages() {
		List<String> msgs = Lists.<String>newArrayList();
		if (messages.size() > 0)
			for (int i = 0; i < messages.size(); i++)
				msgs.add(messages.get(i).line.getText());
		else
			return null;
		return msgs;
	}
	
	public List<String> getCommands() {
		List<String> cmds = Lists.<String>newArrayList();
		if (commands.size() > 0)
			for (int i = 0; i < commands.size(); i++)
				cmds.add(commands.get(i).line.getText());
		else
			return null;
		return cmds;
	}
	
	enum WritableType {
		Message, Command
	}
	
	public class WritableLineElement {
		private final Minecraft mc;
		private final DonationTypeEntry callbackThinking;
		private final WritableType wtype;
		
		public CustomTextBox line;
		
		private CustomButton delete;
		
		public WritableLineElement(DonationTypeEntry dte, Minecraft mc, WritableType t, String tag) {
			this.mc = mc;
			callbackThinking = dte;
			wtype = t;
			line = new CustomTextBox(2, mc.fontRenderer, 0, 0, 200, 20);
			line.tag = tag;
			line.maxStringLength = Short.MAX_VALUE;
			delete = new CustomButton(2002, 0, 0, 20, true, "-");
		}
		
		public void changeTag(String tag) {
			line.tag = tag;
		}
		
		public void drawElement(int x, int y, int mouseX, int mouseY, float partialTicks) {
			line.drawTextBox(x, y);
			delete.drawButton(mc, x + 210, y + 10, mouseX, mouseY, partialTicks);
		}

		public void keyTyped(char typedChar, int keyCode) {
			if (line.textboxKeyTyped(typedChar, keyCode)) {
				
			}
		}
		
		public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
			line.mouseClicked(mouseX, mouseY, mouseButton);
			if (delete.isClick(mouseX, mouseY))
			{
				if (wtype == WritableType.Message)
					callbackThinking.removeMessage(this);
				else if (wtype == WritableType.Command)
					callbackThinking.removeCommands(this);
				delete.playPressSound(mc.getSoundHandler());
			}
		}
	
	}
}
