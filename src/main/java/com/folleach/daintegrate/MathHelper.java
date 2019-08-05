package com.folleach.daintegrate;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MathHelper {
	public static int convertRange(float value, float From1, float From2, float To1, float To2)
	{
	      return (int)((value-From1)/(From2-From1)*(To2-To1)+To1);
	}
}
