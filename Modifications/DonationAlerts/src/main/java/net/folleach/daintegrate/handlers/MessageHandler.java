package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;
import net.folleach.dontaionalerts.DonationAlertsEvent;

public class MessageHandler implements IHandler<MessageHandlerProperties> {
    @Override
    public String getImplementationId() {
        return "message";
    }

    @Override
    public void handle(DonationAlertsEvent event, MessageHandlerProperties properties) {
        System.out.println(properties.message);
    }
}
