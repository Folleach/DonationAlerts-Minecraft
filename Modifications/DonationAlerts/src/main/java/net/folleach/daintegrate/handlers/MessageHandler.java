package net.folleach.daintegrate.handlers;

import net.folleach.daintegrate.IHandler;
import net.folleach.dontaionalerts.DonationAlertsEvent;

public class MessageHandler implements IHandler<MessageProperties> {
    @Override
    public String getModId() {
        return Constants.ModId;
    }

    @Override
    public String getModUrl() {
        return Constants.ModUrl;
    }

    @Override
    public String getHandlerId() {
        return "message_handler";
    }

    @Override
    public void handle(DonationAlertsEvent event, MessageProperties properties) {
        System.out.println(properties.message);
    }
}
