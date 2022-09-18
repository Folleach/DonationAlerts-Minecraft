package net.folleach.daintegrate;

import net.folleach.dontaionalerts.DonationAlertsEvent;

public interface IHandler<T> {
    void Handle(DonationAlertsEvent event, T properties);
}
