package com.folleach.gui;

import com.folleach.daintegrate.Palette;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultButton extends CustomButton {

	public DefaultButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onPress) {
		super(x, y, widthIn, heightIn, buttonText, onPress);
		//super.DefaultBackgroundColor = Palette.BLACK_TRANSPARENT_xB0;
		//super.HoveredBackgroundColor = Palette.YELLOW_TRANSPARENT_xA0;
		//super.HoveredForegroundColor = Palette.WHITE;
	}
	
	public DefaultButton(int x, int y, int widthIn, boolean visibility, String buttonText, IPressable onPress) {
		this(x, y, widthIn, 20, buttonText, onPress);
		visible = visibility;
	}

	public class DefaultButtonBuilder {
		private DefaultButton button;

		public DefaultButtonBuilder(int x, int y, String text, IPressable onPress) {
			button = new DefaultButton(x, y, 0, 0, text, onPress);
		}

		public DefaultButtonBuilder withGreen() {
			button.DefaultBackgroundColor = Palette.GREEN;
			button.HoveredBackgroundColor = Palette.GREEN_HOVERED;
			button.HoveredForegroundColor = Palette.WHITE;
			return this;
		}

		public DefaultButtonBuilder asSquare() {
			button.height = button.width = 20;
			return this;
		}

		public DefaultButtonBuilder asRectangle() {
			button.height = 20;
			button.width = 120;
			return this;
		}

		public DefaultButton build() {
			return button;
		}
	}
}
