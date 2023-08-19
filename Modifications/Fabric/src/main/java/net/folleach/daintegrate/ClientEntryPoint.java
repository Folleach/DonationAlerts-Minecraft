package net.folleach.daintegrate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.folleach.daintegrate.listeners.IListener;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClientEntryPoint implements ClientModInitializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(Constants.ModId);

	@Override
	public void onInitializeClient() {
		DonationAlertsIntegrate donationAlerts = null;
		try {
			donationAlerts = DonationAlertsEntryPoint.Main("donation-alerts-integrate", "settings.json", new IListener<String>() {
				@Override
				public void onValue(String value) {
					LOGGER.info(value);
				}
			});
		} catch (IOException e) {
			LOGGER.error("failed to initialize donation alerts integrate", e);
		}
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			registerCommands(dispatcher);
		});
		var keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.daintegratew.open",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_EQUAL,
				"category.daintegratew.keys"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (keyBinding.wasPressed() && client.player != null) {
				client.player.sendMessage(Text.literal("clicked to key!"));
			}
		});
	}

	private void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal(Constants.ModDefaultCommand)
				.executes(this::editCommand));
	}

	private int editCommand(CommandContext<ServerCommandSource> context)
	{
		context.getSource().sendMessage(Text.literal("opened"));
		return 0;
	}
}
