package net.folleach.testconsole;

import net.folleach.daintegrate.IHandler;
import net.folleach.daintegrate.handlers.CommandHandlerProperties;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class CommandHandlerTest implements IHandler<CommandHandlerProperties> {
    @Override
    public void handle(ReadOnlyDonationAlertsEvent event, CommandHandlerProperties properties) {
    }

    @Override
    public String getImplementationId() {
        return CommandHandlerProperties.ImplementationId;
    }
}
