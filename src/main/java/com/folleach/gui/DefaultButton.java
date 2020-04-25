package com.folleach.gui;

import com.folleach.daintegrate.Palette;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultButton extends CustomButton {

	public DefaultButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onPress) {
		super(x, y, widthIn, heightIn, buttonText, onPress);
		super.DefaultBackgroundColor = Palette.BLACK_TRANSPARENT_xB0;
		super.HoveredBackgroundColor = Palette.YELLOW_TRANSPARENT_xA0;
		super.HoveredForegroundColor = Palette.WHITE;
	}
	
	public DefaultButton(int x, int y, int widthIn, boolean visibility, String buttonText, IPressable onPress) {
		this(x, y, widthIn, 20, buttonText, onPress);
		visible = visibility;
	}
}
