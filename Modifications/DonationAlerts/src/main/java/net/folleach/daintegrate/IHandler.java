package net.folleach.daintegrate;

import net.folleach.dontaionalerts.DonationAlertsEvent;

public interface IHandler<T> extends IImplementationId {
    void handle(DonationAlertsEvent event, T properties);
}
