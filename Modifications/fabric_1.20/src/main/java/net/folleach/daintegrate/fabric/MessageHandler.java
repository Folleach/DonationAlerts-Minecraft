package net.folleach.daintegrate.fabric;

import net.folleach.daintegrate.IHandler;
import net.folleach.daintegrate.handlers.MessageHandlerProperties;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class MessageHandler implements IHandler<MessageHandlerProperties> {
    @Override
    public void handle(ReadOnlyDonationAlertsEvent event, MessageHandlerProperties properties) {
        var player = MinecraftClient.getInstance().player;
        if (player == null)
            return;
        player.sendMessage(Text.literal(properties.message));
    }

    @Override
    public String getImplementationId() {
        return MessageHandlerProperties.ImplementationId;
    }
}
