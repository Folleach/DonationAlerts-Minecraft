package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;
import net.folleach.dontaionalerts.DonationAlertsEvent;

public class CommandHandler implements IHandler<CommandHandlerProperties> {
    @Override
    public void handle(DonationAlertsEvent event, CommandHandlerProperties properties) {

    }

    @Override
    public String getImplementationId() {
        return "command";
    }
}
