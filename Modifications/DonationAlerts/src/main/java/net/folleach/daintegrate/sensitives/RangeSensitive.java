package net.folleach.daintegrate.sensitives;

import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class RangeSensitive implements ISensitive<RangeSensitiveProperties> {
    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, RangeSensitiveProperties properties) {
        return event.getAmount() >= properties.from
                && event.getAmount() <= properties.to
                && event.getCurrency().trim().equalsIgnoreCase(properties.currency.trim());
    }

    @Override
    public String getImplementationId() {
        return "range";
    }
}
