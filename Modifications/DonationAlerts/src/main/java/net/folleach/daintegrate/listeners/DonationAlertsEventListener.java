package net.folleach.daintegrate.listeners;

import net.folleach.dontaionalerts.DonationAlertsEvent;

public class DonationAlertsEventListener implements IListener<DonationAlertsEvent> {
    @Override
    public void onValue(DonationAlertsEvent value) {
        System.out.print("New event from: " + value.UserName);
    }
}
