package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class MessageHandler implements IHandler<MessageHandlerProperties> {
    @Override
    public String getImplementationId() {
        return "message";
    }

    @Override
    public void handle(ReadOnlyDonationAlertsEvent event, MessageHandlerProperties properties) {
        System.out.println("Message receive: " + properties.message);
    }
}
