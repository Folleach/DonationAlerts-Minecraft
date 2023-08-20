package net.folleach.daintegrate.sensitives;

import net.folleach.dontaionalerts.AlertType;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.HashSet;

public class SubscribeSensitive implements ISensitive<SubscribeSensitiveProperties> {
    private final HashSet<String> all;

    public SubscribeSensitive() {
        all = new HashSet<>();
        for (AlertType c : AlertType.values()) {
            all.add(c.name().trim());
        }
    }

    @Override
    public String getImplementationId() {
        return "subscribe";
    }

    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, SubscribeSensitiveProperties properties) {
        return all.contains(properties.type) && event.getType() == AlertType.valueOf(properties.type);
    }
}
