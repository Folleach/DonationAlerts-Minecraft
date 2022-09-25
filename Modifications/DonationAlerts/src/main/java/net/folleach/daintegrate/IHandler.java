package net.folleach.daintegrate;

import net.folleach.dontaionalerts.DonationAlertsEvent;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public interface IHandler<T> extends IImplementationId {
    void handle(ReadOnlyDonationAlertsEvent event, T properties);
}
