package net.folleach.daintegrate.listeners;

import net.folleach.dontaionalerts.DonationAlertsEvent;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.ArrayList;
import java.util.Iterator;

public class DonationAlertsEventListener implements IListener<DonationAlertsEvent> {
    private final ArrayList<IListener<ReadOnlyDonationAlertsEvent>> listeners;
    private final IListener<ReadOnlyDonationAlertsEvent> processor;

    public DonationAlertsEventListener(
            IListener<ReadOnlyDonationAlertsEvent> processor,
            Iterator<IListener<ReadOnlyDonationAlertsEvent>> listeners) {
        this.listeners = new ArrayList<>();
        if (listeners != null)
            listeners.forEachRemaining(this::addListener);
        this.processor = processor;
    }

    public void addListener(IListener<ReadOnlyDonationAlertsEvent> listener) {
        listeners.add(listener);
    }

    @Override
    public void onValue(DonationAlertsEvent value) {
        var readOnly = new ReadOnlyDonationAlertsEvent(value);
        for (IListener<ReadOnlyDonationAlertsEvent> listener : listeners) {
            listener.onValue(readOnly);
        }
        processor.onValue(readOnly);
    }
}
