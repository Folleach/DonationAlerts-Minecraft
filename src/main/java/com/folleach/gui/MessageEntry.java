package com.folleach.gui;

import java.awt.image.BufferedImage;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.folleach.daintegrate.Main;
import com.folleach.daintegrate.Pallete;
import com.folleach.donationalerts.Donation;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MessageEntry extends Gui implements IEntry {
	private FontRenderer fontRenderer;
	private String title;
	private List<String> messageLines;
	private int width;

    public MessageEntry(FontRenderer fontR, Donation donat, int width)
    {
        fontRenderer = fontR;
        this.width = width;
        title = donat.UserName + " - " + donat.Amount + " " + donat.Currency;
        messageLines = fontRenderer.listFormattedStringToWidth(donat.Message, width);
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
		drawRect(x, offset, x + width, offset + 1, Pallete.GRAY30);
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
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}
}
