package net.folleach.daintegrate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ClientEntryPoint implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
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
}
