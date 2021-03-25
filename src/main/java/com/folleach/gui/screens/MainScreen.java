package com.folleach.gui.screens;


import com.folleach.daintegrate.DataCollector;
import com.folleach.daintegrate.DonationAlertsIntegrate;
import com.folleach.daintegrate.Main;
import com.folleach.daintegrate.Palette;
import com.folleach.donationalerts.DonationAlerts;
import com.folleach.donationalerts.DonationAlertsEvent;
import com.folleach.gui.ChapterButton;
import com.folleach.gui.CustomButton;
import com.folleach.gui.MessageEntry;
import com.folleach.gui.ScrollPanel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class MainScreen extends Screen {
    private Screen previous;

    private DonationAlertsIntegrate donationAlertsIntegrate;
    private DonationAlerts donationAlerts;
    private DataCollector data;

    private ResourceLocation donationAlertsLogo;
    private int logoWidth = 552;
    private int logoHeight = 640;
    private Minecraft minecraft;

    private ScrollPanel<MessageEntry> messages;

    public MainScreen(Screen previous, ITextComponent titleIn, DonationAlertsIntegrate donationAlertsIntegrate, DonationAlerts donationAlerts, DataCollector data) {
        super(titleIn);
        this.previous = previous;
        this.donationAlertsIntegrate = donationAlertsIntegrate;
        this.donationAlerts = donationAlerts;
        this.data = data;
        donationAlertsLogo = new ResourceLocation(Main.MODID, "textures/da_logo.png");
        minecraft = Minecraft.getInstance();
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    protected void init() {
        super.init();
        int buttonY = 20;
        addButton(new ChapterButton(10, buttonY += 20,
                I18n.format("daintegratew.status"),
                new ResourceLocation(Main.MODID, "textures/icon_connection.png"),
                b -> minecraft.displayGuiScreen(new ConnectionScreen(this, donationAlerts, data))));
        addButton(new ChapterButton(10, buttonY += 20,
                I18n.format("daintegratew.types"),
                new ResourceLocation(Main.MODID, "textures/icon_types.png"),
                b -> minecraft.displayGuiScreen(new TypesScreen(this, data))));
        addButton(new ChapterButton(10, buttonY += 20,
                I18n.format("daintegratew.help"),
                new ResourceLocation(Main.MODID, "textures/icon_help.png"),
                b -> minecraft.displayGuiScreen(new HelpScreen(this))));
        addButton(new ChapterButton(10, buttonY += 20,
                I18n.format("daintegratew.settings"),
                new ResourceLocation(Main.MODID, "textures/icon_settings.png"),
                b -> minecraft.displayGuiScreen(new SettingsScreen(this, data, donationAlertsIntegrate))));
        addButton(new ChapterButton(10, buttonY += 20,
                I18n.format("daintegratew.acknowledgements"),
                new ResourceLocation(Main.MODID, "textures/icon_supporters.png"),
                b -> minecraft.displayGuiScreen(new SupportersScreen(this))));

        messages = new ScrollPanel<MessageEntry>(140, 40, this.width, this.height);
        for (DonationAlertsEvent event : donationAlertsIntegrate.Donations) {
            messages.addEntry(new MessageEntry(0, 0, messages, event));
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        fill(matrixStack,0, 0, super.width, super.height, Palette.Background);
        messages.drawPanel(matrixStack, mouseX, mouseY, partialTicks);
        fill(matrixStack,0, 0, super.width, 40, Palette.Background);
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.donationAlertsLogo);
        int i = 10;
        int j = 10;
        this.blit(matrixStack, i, j, 0, 0, logoWidth / 32, logoHeight / 32, logoWidth / 32, logoHeight / 32);
        font.drawStringWithShadow(matrixStack, "Donation Alerts Integrate", 35, 14, Palette.BrandOrange);
        font.drawStringWithShadow(matrixStack, "v" + Main.MODVERSION, 10, height - 20, Palette.AlmostWhiteButNotWhite);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        messages.mouseScrolled(mouseX, mouseY, delta);
        return true;
    }
}
