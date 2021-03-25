package com.folleach.gui.screens;

import com.folleach.daintegrate.Action;
import com.folleach.daintegrate.DataCollector;
import com.folleach.daintegrate.Palette;
import com.folleach.donationalerts.DonationAlerts;
import com.folleach.donationalerts.DonationType;
import com.folleach.donationalerts.TypesManager;
import com.folleach.gui.DonationTypeEntry;
import com.folleach.gui.ScrollPanel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class TypesScreen extends TitledScreen {
    private ScrollPanel<DonationTypeEntry> typesPanel;
    private FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
    private Minecraft minecraft = Minecraft.getInstance();
    private DataCollector data;

    public TypesScreen(Screen parent, DataCollector data) {
        super(parent, new StringTextComponent(I18n.format("daintegratew.types")), true);
        this.data = data;
    }

    private void InitializeTypes()
    {
        typesPanel = new ScrollPanel<DonationTypeEntry>(125, 25, this.width, this.height);
        for (int i = 0; i < data.typesManager.getTypes().size(); i++)
            typesPanel.addEntry(new DonationTypeEntry(0, 0, 0, 0, typesPanel, data.typesManager.getTypes().get(i), minecraft, this.width));
    }

    private void TypesAddClick(Button button)
    {
        typesPanel.addEntry(new DonationTypeEntry(0, 0, 0, 0, typesPanel, new DonationType(), minecraft, this.width));
    }

    @Override
    protected void init() {
        super.init();

        InitializeTypes();
    }

    @Override
    public void renderScreenContent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        typesPanel.drawPanel(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return super.charTyped(codePoint, modifiers) || typesPanel.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || typesPanel.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button) || typesPanel.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta) || typesPanel.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void save() {
        if (!canSave())
        {
            setInfoMessage(I18n.format("daintegratew.error"));
            return;
        }
        TypesManager tManager = new TypesManager();
        DonationType temp;
        List<DonationTypeEntry> entries = typesPanel.getEntries();
        try
        {
            for (int i = 0; i < entries.size(); i++)
            {
                temp = new DonationType();
                temp.Name = entries.get(i).getName();
                temp.Active = entries.get(i).getActive();
                temp.CurrencyBRL = (float) Double.parseDouble(entries.get(i).CurrencyBRL.getText());
                temp.CurrencyBYN = (float) Double.parseDouble(entries.get(i).CurrencyBYN.getText());
                temp.CurrencyEUR = (float) Double.parseDouble(entries.get(i).CurrencyEUR.getText());
                temp.CurrencyKZT = (float) Double.parseDouble(entries.get(i).CurrencyKZT.getText());
                temp.CurrencyRUB = (float) Double.parseDouble(entries.get(i).CurrencyRUB.getText());
                temp.CurrencyUAH = (float) Double.parseDouble(entries.get(i).CurrencyUAH.getText());
                temp.CurrencyUSD = (float) Double.parseDouble(entries.get(i).CurrencyUSD.getText());
                if (entries != null)
                    for (String value : entries.get(i).getMessages()) {
                        temp.addAction(new Action(
                                "message",
                                0,
                                new JSONObject().put("message", value)));
                    }
                if (entries != null)
                    for (String value : entries.get(i).getCommands()) {
                        temp.addAction(new Action(
                                "command",
                                0,
                                new JSONObject().put("command", value)));
                    }
                tManager.getTypes().add(temp);
            }
            data.typesManager = tManager;
            data.Save();
            setInfoMessage(I18n.format("daintegratew.saved"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            setInfoMessage(I18n.format("daintegratew.error"));
        }
    }

    @Override
    public boolean canSave() {
        List<DonationTypeEntry> entries = typesPanel.getEntries();
        for (DonationTypeEntry element : entries)
            if (element.hasError)
                return false;
        return true;
    }
}
