package net.folleach.daintegrate.fabric;

import net.folleach.daintegrate.IHandler;
import net.folleach.daintegrate.handlers.CommandHandlerProperties;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class CommandHandler implements IHandler<CommandHandlerProperties> {
    @Override
    public void handle(ReadOnlyDonationAlertsEvent event, CommandHandlerProperties properties) {
        var player = MinecraftClient.getInstance().player;
        if (player == null)
            return;
        player.sendMessage(Text.literal(properties.command));
    }

    @Override
    public String getImplementationId() {
        return CommandHandlerProperties.ImplementationId;
    }
}
