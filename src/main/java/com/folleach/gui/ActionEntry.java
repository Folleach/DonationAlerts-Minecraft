package com.folleach.gui;

import com.folleach.daintegrate.Action;
import com.folleach.daintegrate.DonationAlertsIntegrate;
import com.folleach.daintegrate.Requirement;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ActionEntry extends Widget implements IEntry {

    private Action action;
    private String actionTitle;
    private Requirement[] requirements;

    private List<Widget> inputs = new ArrayList<Widget>();
    private int height = 10;
    private FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

    public ActionEntry(DonationAlertsIntegrate donationAlertsIntegrate,
                       Action action,
                       String actionTitle,
                       int x,
                       int y,
                       int width,
                       int height,
                       ITextComponent title) {
        super(x, y, width, height, title);
        this.action = action;
        this.actionTitle = actionTitle;
        requirements = donationAlertsIntegrate.getExecutor(action.executor).getRequires();
        for (Requirement requirement : requirements) {
            if (requirement.type == Requirement.RequirementType.String) {
                inputs.add(new CustomTextBox(fontRenderer, 0, 0, 200, 20, requirement.name));
                height += 35;
            }
        }
    }

    @Override
    public void drawEntry(MatrixStack matrixs, int x, int y, int mouseX, int mouseY, float partialTicks) {
        fontRenderer.drawString(matrixs, "", 1, 1, 2);
        for (Widget widget : inputs) {
            widget.renderButton(matrixs, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public int getHeight() {
        return 0;
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
