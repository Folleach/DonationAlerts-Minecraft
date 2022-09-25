package net.folleach.daintegrate.sensitives;

import net.folleach.daintegrate.None;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class AlwaysSensitive implements ISensitive<None> {
    @Override
    public String getImplementationId() {
        return "always";
    }

    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, None properties) {
        return true;
    }
}
