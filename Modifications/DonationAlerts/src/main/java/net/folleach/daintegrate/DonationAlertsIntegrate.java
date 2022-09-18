package net.folleach.daintegrate;

import net.folleach.daintegrate.handlers.HandlerExtensions;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class DonationAlertsIntegrate {
    public static final DonationAlertsIntegrate Instance = new DonationAlertsIntegrate();

    private ConcurrentHashMap<String, HandlerDescriptor> handlers = new ConcurrentHashMap<>();
    private ArrayList<IListener<ReadOnlyDonationAlertsEvent>> listeners = new ArrayList<>();

    public DonationAlertsIntegrate()
    {
    }

    public static <T extends IHandler> void registerHandler(T handler) {
        var id = HandlerExtensions.getUniqueHandlerId(handler);
        var descriptor = new HandlerDescriptor<T>(handler);
        Instance.handlers.put(id, descriptor);
    }

    public static void registerEventListener(IListener<ReadOnlyDonationAlertsEvent> listener) {
        Instance.listeners.add(listener);
    }

    static Iterator<HandlerDescriptor> getHandlers() {
        return Instance.handlers.elements().asIterator();
    }

    public static Iterator<IListener<ReadOnlyDonationAlertsEvent>> getEventListeners() {
        return Instance.listeners.iterator();
    }
}
