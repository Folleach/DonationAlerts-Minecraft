package com.folleach.daintegrate;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Palette
{
	public static final int WHITE = 0xFFFFFFFF;
	public static final int GRAY60 = 0xFF606060;
	public static final int GRAY30 = 0xFF303030;
	public static final int GRAY12 = 0xFF121212;
	public static final int GRAY30_TRANSPARENT_xDD = 0xDD303030;
	public static final int GRAY12_TRANSPARENT_xDD = 0xDD121212;
	public static final int YELLOW = 0xFFF59907;
	public static final int YELLOW_TRANSPARENT_xA0 = 0xA0F59907;
	public static final int YELLOWTRANSPERIENTDIM = 0x70FFA024;
	public static final int GREEN = 0xFF3CAB3C;
	public static final int GREEN_HOVERED = 0xFF64BA64;
	public static final int RED = 0xFFBF2030;
	public static final int RED_HOVERED = 0xFFC1414E;

	public static final int BrandOrange = 0xFFF57D07;
	public static final int AlmostWhiteButNotWhite = 0xFFF1F1F1;
	public static final int Gray = 0xFF303030;
	public static final int Grayish = 0xFF262626;
	public static final int Background = 0xFF1F1F1F;
	public static final int VeryDark = 0xFF191919;
	
	public static final int BLACK_TRANSPARENT30 = 0x50000000;   //FIXME: 30 -> 50?
	public static final int BLACK_TRANSPARENT60 = 0x60000000;
	public static final int BLACK_TRANSPARENT90 = 0x70000000;   //FIXME: 90 -> 70?
	public static final int BLACK_TRANSPARENT_xB0 = 0x80000000;
}
