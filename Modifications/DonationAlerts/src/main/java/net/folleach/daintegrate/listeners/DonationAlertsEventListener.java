package net.folleach.daintegrate.listeners;

import net.folleach.daintegrate.DonationAlertsIntegrate;
import net.folleach.dontaionalerts.DonationAlertsEvent;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class DonationAlertsEventListener implements IListener<DonationAlertsEvent> {
    private final IListener<ReadOnlyDonationAlertsEvent> processor;

    public DonationAlertsEventListener(IListener<ReadOnlyDonationAlertsEvent> processor) {
        this.processor = processor;
    }

    @Override
    public void onValue(DonationAlertsEvent value) {
        var readOnly = new ReadOnlyDonationAlertsEvent(value);
        DonationAlertsIntegrate.getEventListeners().forEachRemaining(listener -> {
            listener.onValue(readOnly);
        });
        processor.onValue(readOnly);
    }
}
