package com.folleach.gui;

import java.util.List;

import com.folleach.daintegrate.Action;
import com.folleach.daintegrate.Palette;
import com.folleach.donationalerts.DonationType;
import com.google.common.collect.Lists;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DonationTypeEntry extends Widget implements IEntry {
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
	
	public DonationTypeEntry(int x, int y, int width, int height, ScrollPanel own, DonationType type, Minecraft mc, int right)
	{

		super(x, y, width, height, new StringTextComponent("msg"));
		this.right = right;
		owner = own;
		messages = Lists.<WritableLineElement>newArrayList();
		commands = Lists.<WritableLineElement>newArrayList();

		addMessage = new CustomButton(0, 0, 20, true, "+", this::AddMessageClick);
		addMessage.DefaultBackgroundColor = Palette.GREEN;
		addMessage.HoveredBackgroundColor = Palette.GREEN_HOVERED;
		addMessage.HoveredForegroundColor = Palette.WHITE;
		addCommand = new CustomButton(0, 0, 20, true, "+", this::AddCommandClick);
		addCommand.DefaultBackgroundColor = Palette.GREEN;
		addCommand.HoveredBackgroundColor = Palette.GREEN_HOVERED;
		addCommand.HoveredForegroundColor = Palette.WHITE;
		
		this.type = type;
		this.fontRenderer = mc.fontRenderer;
		this.mc = mc;
		textBoxName = new CustomTextBox(fontRenderer, 0, 0, 200, 20, "");
		textBoxName.tag = I18n.format("daintegratew.name");
		textBoxName.setText(type.Name);
		checkBoxActive = new CheckBox(0, 0, 60, true, I18n.format("daintegratew.active"), type.Active, this::CheckBoxClick);
		
		deleteEntry = new CustomButton(0, 0, 20, true, "-", this::DeleteEntryClick);
		deleteEntry.DefaultBackgroundColor = Palette.RED;
		deleteEntry.HoveredBackgroundColor = Palette.RED_HOVERED;
		deleteEntry.HoveredForegroundColor = Palette.WHITE;
		WritableLineElement temp;
		for (int i = 0; i < type.getActions().size(); i++) {
			Action action = type.getActions().get(i);
			if (action.executor.equals("message")) {
				temp = new WritableLineElement(this, mc, WritableType.Message, langLine + " " + (i + 1));
				temp.line.setText(action.data.getString("message"));
				messages.add(temp);
			}
			else if (action.executor.equals("command")) {
				temp = new WritableLineElement(this, mc, WritableType.Command, langLine + " " + (i + 1));
				temp.line.setText(action.data.getString("command"));
				commands.add(temp);
			}
		}
		
		CurrencyBRL = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyBRL.tag = "BRL";
		CurrencyBRL.setText(String.valueOf(type.CurrencyBRL));
		CurrencyBYN = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyBYN.tag = "BYN";
		CurrencyBYN.setText(String.valueOf(type.CurrencyBYN));
		CurrencyEUR = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyEUR.tag = "EUR";
		CurrencyEUR.setText(String.valueOf(type.CurrencyEUR));
		CurrencyKZT = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyKZT.tag = "KZT";
		CurrencyKZT.setText(String.valueOf(type.CurrencyKZT));
		CurrencyRUB = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyRUB.tag = "RUB";
		CurrencyRUB.setText(String.valueOf(type.CurrencyRUB));
		CurrencyUAH = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyUAH.tag = "UAH";
		CurrencyUAH.setText(String.valueOf(type.CurrencyUAH));
		CurrencyUSD = new CustomTextBox(fontRenderer, 0, 0, 80, 20, "");
		CurrencyUSD.tag = "USD";
		CurrencyUSD.setText(String.valueOf(type.CurrencyUSD));
	}

	private void AddMessageClick(Button button)
	{
		messages.add(new WritableLineElement(this, mc, WritableType.Message, langLine + " " + (messages.size() + 1)));
		button.playDownSound(mc.getSoundHandler());
		owner.updateHeight();
	}

	private void AddCommandClick(Button button)
	{
		commands.add(new WritableLineElement(this, mc, WritableType.Command, langLine + " " + (commands.size() + 1)));
		button.playDownSound(mc.getSoundHandler());
		owner.updateHeight();
	}

	private void DeleteEntryClick(Button button)
	{
		button.playDownSound(mc.getSoundHandler());
		owner.removeEntry(this);
	}

	private void CheckBoxClick(Button button)
	{
		checkBoxActive.SwitchFlag(!checkBoxActive.Flag);
		checkBoxActive.playDownSound(mc.getSoundHandler());
	}

	@Override
	public void drawEntry(MatrixStack matrixs, int x, int y, int mouseX, int mouseY, float partialTicks) {
		int offset = y;
		textBoxName.renderButton(matrixs, x, offset);
		checkBoxActive.drawButton(matrixs, mc, x + 210, y + 10, mouseX, mouseY, partialTicks);
		deleteEntry.drawButton(matrixs, mc, x + 275, y + 10, mouseX, mouseY, partialTicks);
		offset += 35;
		fontRenderer.drawString(matrixs, langMessages, x, offset, Palette.WHITE);
		offset += 10;
		for (int i = 0; i < messages.size(); i++)
		{
			messages.get(i).drawElement(matrixs, x, offset, mouseX, mouseY, partialTicks);
			offset += 35;
		}
		addMessage.drawButton(matrixs, mc, x + 210, offset, mouseX, mouseY, partialTicks);
		offset += 25;
		fontRenderer.drawString(matrixs, langCommands, x, offset, Palette.WHITE);
		offset += 10;
		for (int i = 0; i < commands.size(); i++)
		{
			commands.get(i).drawElement(matrixs, x, offset, mouseX, mouseY, partialTicks);
			offset += 35;
		}
		addCommand.drawButton(matrixs, mc, x + 210, offset, mouseX, mouseY, partialTicks);
		offset += 25;
		CurrencyUSD.renderButton(matrixs, x, offset); CurrencyRUB.renderButton(matrixs, x + 90, offset); CurrencyEUR.renderButton(matrixs, x + 90 * 2, offset);
		offset += 35;
		CurrencyKZT.renderButton(matrixs, x, offset); CurrencyBRL.renderButton(matrixs, x + 90, offset); CurrencyBYN.renderButton(matrixs, x + 90 * 2, offset);
		offset += 35;
		CurrencyUAH.renderButton(matrixs, x, offset);
		offset += 35;
		fill(matrixs, x, offset, right - 10, offset + 1, Palette.GRAY60);
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
	public void keyType(char typedChar, int keyCode) {
		for (WritableLineElement element : messages)
			element.keyTyped(typedChar, keyCode);
		for (WritableLineElement element : commands)
			element.keyTyped(typedChar, keyCode);
		
		textBoxName.charTyped(typedChar, keyCode);
		if (CurrencyBRL.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyBRL);
		if (CurrencyBYN.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyBYN);
		if (CurrencyEUR.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyEUR);
		if (CurrencyKZT.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyKZT);
		if (CurrencyRUB.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyRUB);
		if (CurrencyUAH.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyUAH);
		if (CurrencyUSD.charTyped(typedChar, keyCode))
			checkCurrency(CurrencyUSD);
	}

	public boolean keyPress(int a, int b, int c)
	{
		for (WritableLineElement element : messages)
			element.keyPressed(a, b, c);
		for (WritableLineElement element : commands)
			element.keyPressed(a, b, c);
		textBoxName.keyPressed(a, b, c);
		if (CurrencyBRL.keyPressed(a, b, c))
			checkCurrency(CurrencyBRL);
		if (CurrencyBYN.keyPressed(a, b, c))
			checkCurrency(CurrencyBYN);
		if (CurrencyEUR.keyPressed(a, b, c))
			checkCurrency(CurrencyEUR);
		if (CurrencyKZT.keyPressed(a, b, c))
			checkCurrency(CurrencyKZT);
		if (CurrencyRUB.keyPressed(a, b, c))
			checkCurrency(CurrencyRUB);
		if (CurrencyUAH.keyPressed(a, b, c))
			checkCurrency(CurrencyUAH);
		if (CurrencyUSD.keyPressed(a, b, c))
			checkCurrency(CurrencyUSD);
		return true;
	}

	public boolean mouseClick(double mouseX, double mouseY, int mouseButton)
	{
		for (int i = 0; i < messages.size(); i++)
			messages.get(i).mouseClicked(mouseX, mouseY, mouseButton);

		for (int i = 0; i < commands.size(); i++)
			commands.get(i).mouseClicked(mouseX, mouseY, mouseButton);
		textBoxName.mouseClicked(mouseX, mouseY, mouseButton);

		addMessage.mouseClicked(mouseX, mouseY, mouseButton);
		addCommand.mouseClicked(mouseX, mouseY, mouseButton);
		deleteEntry.mouseClicked(mouseX, mouseY, mouseButton);
		checkBoxActive.mouseClicked(mouseX, mouseY, mouseButton);

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
		return true;
	}
	
	private void checkCurrency(CustomTextBox textBox) {
		try {
			String aa = textBox.getText();
			Double.parseDouble(aa);
			textBox.LineColor = Palette.GRAY30_TRANSPARENT_xDD;
			hasError = false;
		}
		catch (Exception e) {
			textBox.LineColor = Palette.RED;
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
		for (int i = 0; i < messages.size(); i++)
			msgs.add(messages.get(i).line.getText());
		return msgs;
	}
	
	public List<String> getCommands() {
		List<String> cmds = Lists.<String>newArrayList();
		for (int i = 0; i < commands.size(); i++)
			cmds.add(commands.get(i).line.getText());
		return cmds;
	}
	
	enum WritableType {
		Message, Command
	}
	
	public class WritableLineElement
	{
		private final Minecraft mc;
		private final DonationTypeEntry callbackThinking;
		private final WritableType wtype;
		
		public CustomTextBox line;
		
		private CustomButton delete;
		
		public WritableLineElement(DonationTypeEntry dte, Minecraft mc, WritableType t, String tag)
		{
			this.mc = mc;
			callbackThinking = dte;
			wtype = t;
			line = new CustomTextBox(mc.fontRenderer, 0, 0, 200, 20, "");
			line.tag = tag;
			delete = new CustomButton(0, 0, 20, true, "-", this::DeleteClick);
		}

		public void DeleteClick(Button button)
		{
			if (wtype == WritableType.Message)
				callbackThinking.removeMessage(this);
			else if (wtype == WritableType.Command)
				callbackThinking.removeCommands(this);
			button.playDownSound(mc.getSoundHandler());
			owner.updateHeight();
		}

		public void changeTag(String tag) {
			line.tag = tag;
		}
		
		public void drawElement(MatrixStack matrixs, int x, int y, int mouseX, int mouseY, float partialTicks)
		{
			line.renderButton(matrixs, x, y);
			delete.drawButton(matrixs, mc, x + 210, y + 10, mouseX, mouseY, partialTicks);
		}

		public void keyTyped(char typedChar, int keyCode)
		{
			if (line.charTyped(typedChar, keyCode))
			{
			}
		}

		public boolean keyPressed(int a, int b, int c)
		{
			if (line.keyPressed(a, b, c))
				return true;
			return false;
		}
		
		public void mouseClicked(double mouseX, double mouseY, int mouseButton)
		{
			line.mouseClicked(mouseX, mouseY, mouseButton);
			delete.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
}
