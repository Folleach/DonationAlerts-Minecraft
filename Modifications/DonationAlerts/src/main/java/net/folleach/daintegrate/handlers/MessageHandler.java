package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;
import net.folleach.dontaionalerts.DonationAlertsEvent;

public class MessageHandler implements IHandler<MessageProperties> {
    @Override
    public String getImplementationId() {
        return "message_handler";
    }

    @Override
    public void handle(DonationAlertsEvent event, MessageProperties properties) {
        System.out.println(properties.message);
    }
}
