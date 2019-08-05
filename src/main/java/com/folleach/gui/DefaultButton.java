package com.folleach.gui;

import com.folleach.daintegrate.Pallete;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultButton extends CustomButton {

	public DefaultButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		super.DefaultBackgroundColor = Pallete.BLACK_TRANSPERIENTB0;
		super.HoveredBackgroundColor = Pallete.YELLOW_TRANSPERIENTA0;
		super.HoveredForegroundColor = Pallete.WHITE;
	}
	
	public DefaultButton(int buttonId, int x, int y, int widthIn, boolean visibility, String buttonText) {
		this(buttonId, x, y, widthIn, 20, buttonText);
		visible = visibility;
	}
}
