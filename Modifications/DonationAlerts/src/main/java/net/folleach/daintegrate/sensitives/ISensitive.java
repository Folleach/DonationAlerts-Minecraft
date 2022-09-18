package net.folleach.daintegrate.sensitives;

import net.folleach.daintegrate.IImplementationId;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public interface ISensitive<T> extends IImplementationId {
    boolean isActive(ReadOnlyDonationAlertsEvent event, T properties);
}
