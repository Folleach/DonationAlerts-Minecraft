package net.folleach.daintegrate.sensitives;

import net.folleach.dontaionalerts.AlertType;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class TwitchPointsSensitive implements ISensitive<TwitchPointsSensitiveProperties> {
    @Override
    public String getImplementationId() {
        return "twitch/points";
    }

    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, TwitchPointsSensitiveProperties properties) {
        return event.getType() == AlertType.TwitchPoints
                && event.getAmount() >= properties.from
                && event.getAmount() <= properties.to;
    }
}
