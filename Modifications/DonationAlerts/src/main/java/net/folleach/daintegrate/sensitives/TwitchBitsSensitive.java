package net.folleach.daintegrate.sensitives;

import net.folleach.dontaionalerts.AlertType;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

public class TwitchBitsSensitive implements ISensitive<TwitchBitsSensitiveProperties> {
    @Override
    public String getImplementationId() {
        return "twitch/bits";
    }

    @Override
    public boolean isActive(ReadOnlyDonationAlertsEvent event, TwitchBitsSensitiveProperties properties) {
        return event.getType() == AlertType.TwitchBits
                && event.getAmount() >= properties.from
                && event.getAmount() <= properties.to;
    }
}
