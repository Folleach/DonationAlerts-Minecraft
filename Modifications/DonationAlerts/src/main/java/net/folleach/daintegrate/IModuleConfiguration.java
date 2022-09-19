package net.folleach.daintegrate;

import net.folleach.daintegrate.listeners.IListener;
import net.folleach.daintegrate.sensitives.ISensitive;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public interface IModuleConfiguration {
    <T> IModuleConfiguration registerHandler(IHandler<T> handler);
    <T> IModuleConfiguration registerSensitive(ISensitive<T> sensitive);
    IModuleConfiguration registerEventListener(IListener<ReadOnlyDonationAlertsEvent> event);
}
