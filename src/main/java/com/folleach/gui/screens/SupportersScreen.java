package com.folleach.gui.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;

public class SupportersScreen extends TitledScreen {
    protected SupportersScreen(Screen parent) {
        super(parent, new StringTextComponent(I18n.format("daintegratew.acknowledgements")), false);
    }
}
