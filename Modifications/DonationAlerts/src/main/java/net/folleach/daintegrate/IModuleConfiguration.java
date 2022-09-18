package net.folleach.daintegrate;

import net.folleach.daintegrate.listeners.IListener;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public interface IModuleConfiguration {
    <T> IModuleConfiguration registerHandler(IHandler<T> handler);
    IModuleConfiguration registerEventListener(IListener<ReadOnlyDonationAlertsEvent> event);
}
