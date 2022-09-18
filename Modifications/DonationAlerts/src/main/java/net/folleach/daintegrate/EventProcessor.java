package net.folleach.daintegrate;

import net.folleach.daintegrate.listeners.IListener;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class EventProcessor implements IListener<ReadOnlyDonationAlertsEvent> {
    @Override
    public void onValue(ReadOnlyDonationAlertsEvent value) {
        System.out.println("Receive new event");
    }
}
