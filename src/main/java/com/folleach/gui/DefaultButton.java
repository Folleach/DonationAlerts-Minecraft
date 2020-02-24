package com.folleach.gui;

import com.folleach.daintegrate.Pallete;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultButton extends CustomButton {

	public DefaultButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onPress) {
		super(x, y, widthIn, heightIn, buttonText, onPress);
		super.DefaultBackgroundColor = Pallete.BLACK_TRANSPERIENTB0;
		super.HoveredBackgroundColor = Pallete.YELLOW_TRANSPERIENTA0;
		super.HoveredForegroundColor = Pallete.WHITE;
	}
	
	public DefaultButton(int x, int y, int widthIn, boolean visibility, String buttonText, IPressable onPress) {
		this(x, y, widthIn, 20, buttonText, onPress);
		visible = visibility;
	}
}
