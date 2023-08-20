package net.folleach.daintegrate.sensitives;

import net.folleach.dontaionalerts.AlertType;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class DonateSensitive implements ISensitive<DonateSensitiveProperties> {
    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, DonateSensitiveProperties properties) {
        return event.getType() == AlertType.Donate
                && event.getAmount() >= properties.from
                && event.getAmount() <= properties.to
                && event.getCurrency().trim().equalsIgnoreCase(properties.currency.trim());
    }

    @Override
    public String getImplementationId() {
        return "donate";
    }
}
