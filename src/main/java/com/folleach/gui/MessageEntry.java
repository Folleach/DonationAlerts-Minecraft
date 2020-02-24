package com.folleach.gui;

import java.util.List;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.folleach.daintegrate.Pallete;
import com.folleach.donationalerts.Donation;
import net.minecraft.client.gui.FontRenderer;

@OnlyIn(Dist.CLIENT)
public class MessageEntry extends Widget implements IEntry
{
	private FontRenderer fontRenderer;
	private String title;
	private List<String> messageLines;

    public MessageEntry(int x, int y, int width, int height, FontRenderer fontR, Donation donate)
    {
		super(x, y, height, width, "msg");
		fontRenderer = fontR;
        title = donate.UserName + " - " + donate.Amount + " " + donate.Currency;
        messageLines = fontRenderer.listFormattedStringToWidth(donate.Message, width);
    }

	@Override
	public void drawEntry(int x, int y, int mouseX, int mouseY, float partialTicks) {
		int offset = y;
		fontRenderer.drawString(title, x, offset, Pallete.WHITE);
		offset += 10;
		for (String element : messageLines)
		{
			fontRenderer.drawString(element, x, offset, Pallete.WHITE);
			offset += 10;
		}
		fill(x, offset, x + width, offset + 1, Pallete.GRAY30);
		offset += 5;
	}

	@Override
	public int getHeight() {
		return 15 + messageLines.size() * 10;
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) {		
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
    	return false;
	}

	@Override
	public boolean keyPressed(int a, int b, int c) {
		return false;
	}
}
