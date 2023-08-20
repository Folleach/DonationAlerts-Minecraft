package net.folleach.testconsole;

import net.folleach.daintegrate.IHandler;
import net.folleach.daintegrate.handlers.MessageHandlerProperties;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class MessageHandlerTest implements IHandler<MessageHandlerProperties> {
    @Override
    public void handle(ReadOnlyDonationAlertsEvent event, MessageHandlerProperties properties) {
    }

    @Override
    public String getImplementationId() {
        return MessageHandlerProperties.ImplementationId;
    }
}
