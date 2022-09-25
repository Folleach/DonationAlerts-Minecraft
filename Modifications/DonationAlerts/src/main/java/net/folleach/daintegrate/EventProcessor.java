package net.folleach.daintegrate;

import net.folleach.daintegrate.configurations.TriggerDto;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.stream.Stream;

public class EventProcessor implements IListener<ReadOnlyDonationAlertsEvent> {
    @Override
    public void onValue(ReadOnlyDonationAlertsEvent value) {
        getSenseTrigger(value).forEach(trigger -> {
            for (var handler : trigger.handlers) {
                var handlerDescriptor = DonationAlertsIntegrate.getHandler(handler.properties.type);
                handlerDescriptor.getHandler().handle(value, handler.properties.value);
            }
        });
    }

    private Stream<TriggerDto> getSenseTrigger(ReadOnlyDonationAlertsEvent event) {
        return DonationAlertsIntegrate.getTriggers().filter(trigger -> {
            for (var sensitive : trigger.sensitives) {
                var sensitiveDescriptor = DonationAlertsIntegrate.getSensitive(sensitive.properties.type);
                var isSense = sensitiveDescriptor.getSensitive().isActive(event, sensitive.properties.value);
                if (isSense)
                    return true;
            }
            return false;
        });
    }
}
