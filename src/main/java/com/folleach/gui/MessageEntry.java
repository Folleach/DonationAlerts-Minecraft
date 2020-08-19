package com.folleach.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.folleach.daintegrate.Palette;
import com.folleach.donationalerts.DonationAlertsEvent;
import net.minecraft.client.gui.FontRenderer;

@OnlyIn(Dist.CLIENT)
public class MessageEntry extends Widget implements IEntry
{
	private FontRenderer fontRenderer;
	private String title;
	private List<String> messageLines;

    public MessageEntry(int x, int y, int width, int height, FontRenderer fontR, DonationAlertsEvent donate)
    {
		super(x, y, height, width, new StringTextComponent("msg"));
		fontRenderer = fontR;
        title = donate.UserName + " - " + donate.Amount + " " + donate.Currency;
        messageLines = ListFormattedStringToWidth(donate.Message, width);
    }

    private List<String> ListFormattedStringToWidth(String value, int width)
	{
		// TODO: Needed optimize
    	List<String> result = new ArrayList<String>();
    	result.add("");
    	int spaceSize = fontRenderer.getStringWidth(" ");
    	String[] array = value.split("\\s+");
    	int currentWidth = 0;
    	for (int i = 0; i < array.length; i++)
		{
			int wordWidth = fontRenderer.getStringWidth(array[i]);
			if (currentWidth + wordWidth + spaceSize > width)
			{
				result.add(array[i]);
				currentWidth = wordWidth + spaceSize;
				continue;
			}
			result.set(result.size() - 1, result.get(result.size() - 1) + " " +  array[i]);
			currentWidth += wordWidth + spaceSize;
		}
    	return result;
	}

	@Override
	public void drawEntry(MatrixStack matrixs, int x, int y, int mouseX, int mouseY, float partialTicks) {
		int offset = y;
		fontRenderer.drawString(matrixs, title, x, offset, Palette.WHITE);
		offset += 10;
		for (String element : messageLines)
		{
			fontRenderer.drawString(matrixs, element, x, offset, Palette.WHITE);
			offset += 10;
		}
		fill(matrixs, x, offset, x + width, offset + 1, Palette.GRAY30);
		offset += 5;
	}

	@Override
	public int getHeight() {
		return 15 + messageLines.size() * 10;
	}

	@Override
	public void keyType(char typedChar, int keyCode) {
	}

	@Override
	public boolean mouseClick(double mouseX, double mouseY, int mouseButton) {
    	return false;
	}

	@Override
	public boolean keyPress(int a, int b, int c) {
		return false;
	}
}
