package net.folleach.daintegrate;

import net.folleach.daintegrate.configurations.HandlerPropertiesDto;
import net.folleach.daintegrate.configurations.TriggerDto;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

public class EventProcessor implements IListener<ReadOnlyDonationAlertsEvent> {
    private long currentTime = 0;
    private final Lock lock = new ReentrantLock();
    private TreeMap<Long, ArrayList<ScheduledHandler>> queue = new TreeMap<>();

    @Override
    public void onValue(ReadOnlyDonationAlertsEvent value) {
        var timeSnapshot = currentTime;
        getSenseTrigger(value).forEach(trigger -> {
            System.out.println("Activate trigger: " + trigger.name);
            for (var handler : trigger.handlers) {
                if (handler.delay > 0)
                {
                    lock.lock();
                    try {
                        var scheduled = timeSnapshot + handler.delay;
                        var array = queue.get(scheduled);
                        if (array == null)
                            array = new ArrayList<>();
                        array.add(new ScheduledHandler(value, handler));
                        queue.put(scheduled, array);
                        continue;
                    }
                    finally {
                        lock.unlock();
                    }
                }
                execute(value, handler);
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

    private void execute(ReadOnlyDonationAlertsEvent value, HandlerPropertiesDto handler) {
        var handlerDescriptor = DonationAlertsIntegrate.getHandler(handler.properties.type);
        handlerDescriptor.getHandler().handle(value, handler.properties.value);
    }

    public void evalute() {
        currentTime += 1;
        lock.lock();
        try {
            while (!queue.isEmpty()) {
                var entry = queue.firstEntry();
                if (entry.getKey() > currentTime)
                    break;
                entry = queue.pollFirstEntry();
                for (var item : entry.getValue()) {
                    execute(item.event, item.handler);
                }
            }
        }
        finally {
            lock.unlock();
        }
    }
}
