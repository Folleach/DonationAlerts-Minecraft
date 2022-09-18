package net.folleach.daintegrate;

import net.folleach.dontaionalerts.DonationAlertsEvent;

public interface IHandler<T> {
    String getModId();
    String getModUrl();
    String getHandlerId();

    void handle(DonationAlertsEvent event, T properties);
}
