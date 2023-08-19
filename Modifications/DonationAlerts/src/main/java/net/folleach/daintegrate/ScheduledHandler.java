package net.folleach.daintegrate;

import net.folleach.daintegrate.configurations.HandlerPropertiesDto;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class ScheduledHandler {
    public ReadOnlyDonationAlertsEvent event;
    public HandlerPropertiesDto handler;

    public ScheduledHandler(ReadOnlyDonationAlertsEvent event, HandlerPropertiesDto handler) {
        this.event = event;
        this.handler = handler;
    }
}
