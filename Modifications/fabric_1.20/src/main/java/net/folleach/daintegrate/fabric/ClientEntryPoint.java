package net.folleach.daintegrate.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.folleach.daintegrate.Constants;
import net.folleach.daintegrate.DonationAlertsIntegrate;
import net.folleach.daintegrate.DonationAlertsIntegrateFactory;
import net.folleach.daintegrate.EventProcessor;
import net.folleach.daintegrate.configurations.sources.FileConfigurationSource;
import net.folleach.daintegrate.listeners.DonationAlertsEventListener;
import net.folleach.dontaionalerts.DonationAlertsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ClientEntryPoint implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Constants.ModId);
    private static FileConfigurationSource configurationSource = null;
    private DonationAlertsClient client;

    @Override
    public void onInitializeClient() {
        configurationSource = null;
        try {
            configurationSource = DonationAlertsIntegrateFactory.create(
                    "donation-alerts-integrate",
                    "settings.yaml",
                    LOGGER::info);
        } catch (IOException e) {
            LOGGER.error("failed to initialize donation-alerts-integrate", e);
        }

        DonationAlertsIntegrate.configure(Constants.ModId, Constants.ModUrl)
                .registerHandler(new MessageHandler())
                .registerHandler(new CommandHandler());

        configurationSource.addListener(settings -> {
            var player = MinecraftClient.getInstance().player;
            if (player == null)
                return;
            if (settings.disableSettingsUpdateMessage)
                return;
            player.sendMessage(getPrefix().append("settings updated with ")
                    .append(String.valueOf(settings.triggers == null ? 0 : settings.triggers.length))
                    .append(" triggers")
            );
        });

        var eventProcessor = new EventProcessor();
        var listener = new DonationAlertsEventListener(eventProcessor);
        try {
            client = new DonationAlertsClient(Constants.DonationAlertsEventServer, listener);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> registerCommands(dispatcher));

        ClientTickEvents.END_CLIENT_TICK.register(t -> {
            eventProcessor.evalute();
        });
    }

    private void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("da")
                .then(ClientCommandManager.literal("set")
                        .executes(this::setCommand)
                )
                .then(ClientCommandManager.literal("connect")
                        .executes(this::connectCommand)
                )
                .then(ClientCommandManager.literal("disconnect")
                        .executes(this::disconnectCommand)
                )
                .then(ClientCommandManager.literal("status")
                        .executes(this::statusCommand)
                )
        );
    }

    private int statusCommand(CommandContext<FabricClientCommandSource> context) {
        return 0;
    }

    private int disconnectCommand(CommandContext<FabricClientCommandSource> context) {
        client.disconnect();
        return 0;
    }

    private int connectCommand(CommandContext<FabricClientCommandSource> context) {
        configurationSource.startListening();
        var home = System.getProperty("user.home");
        var token = "";
        try (BufferedReader br = new BufferedReader(new FileReader(new File(home, Constants.TokenFileName)))) {
            String line;
            while ((line = br.readLine()) != null && line.length() > 3) {
                token = line.trim();
            }
        } catch (FileNotFoundException e) {
            sendTokenNotFoundMessage(context.getSource().getPlayer());
            return 1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (token.isEmpty())
        {
            sendTokenNotFoundMessage(context.getSource().getPlayer());
            return 1;
        }
        var connected = client.connect(token);
        if (connected)
            context.getSource().getPlayer().sendMessage(getPrefix().append("Successfully connected"));
        else
            context.getSource().getPlayer().sendMessage(getPrefix().append("Failed to connect :(").formatted(Formatting.DARK_RED));
        return 0;
    }

    private int setCommand(CommandContext<FabricClientCommandSource> context) {
        try {
            var text = new net.minecraft.client.util.Clipboard().getClipboard(0, (e, d) -> {
                context.getSource().getPlayer().sendMessage(getPrefix().append("Unsupported token assignment method... Paste token to the file: ~/" + Constants.TokenFileName));
            });
            if (text == null || text.trim().length() < 3)
            {
                context.getSource().getPlayer().sendMessage(getPrefix().append("The copied text does not look like a token"));
                return 1;
            }
            text = text.trim();
            var home = System.getProperty("user.home");
            PrintWriter writer = new PrintWriter(new File(home, Constants.TokenFileName), StandardCharsets.UTF_8);
            writer.println(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        context.getSource().getPlayer().sendMessage(getPrefix().append("The token is set"));
//        context
//                .getSource()
//                .getPlayer()
//                .sendMessage(getPrefix().append("Settings stored in ")
//                        .append(Text
//                                .literal("donation-alerts-integrate/settings.json")
//                                .formatted(Formatting.GOLD)
//                        )
//                        .append(Text.literal(". "))
//                        .append(Text.literal("Click to open")
//                                .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, "donation-alerts-integrate/settings.json")))
//                                .formatted(Formatting.AQUA)
//                        )
//                );
        return 0;
    }

    private static void sendTokenNotFoundMessage(ClientPlayerEntity player) {
        player.sendMessage(getPrefix()
                .append("Token not found. ")
                .append(Text
                        .literal("Learn more")
                        .setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Constants.GuideToSetToken)))
                        .formatted(Formatting.AQUA)
                )
        );
    }

    private static MutableText getPrefix() {
        return Text.literal("[")
                .append(Text
                        .literal("DonationAlerts")
                        .formatted(Formatting.GOLD)
                )
                .append("] ");
    }
}
