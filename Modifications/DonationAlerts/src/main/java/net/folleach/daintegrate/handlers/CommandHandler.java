package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class CommandHandler implements IHandler<CommandHandlerProperties> {
    @Override
    public String getImplementationId() {
        return "command";
    }

    @Override
    public void handle(ReadOnlyDonationAlertsEvent event, CommandHandlerProperties properties) {
        System.out.println("Command execute: " + properties.command);
    }
}
