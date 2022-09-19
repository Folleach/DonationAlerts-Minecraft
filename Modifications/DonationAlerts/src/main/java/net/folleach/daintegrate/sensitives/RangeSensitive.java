package net.folleach.daintegrate.sensitives;

import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class RangeSensitive implements ISensitive<RangeSensitiveProperties> {
    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, RangeSensitiveProperties properties) {
        return false;
    }

    @Override
    public String getImplementationId() {
        return "range";
    }
}
