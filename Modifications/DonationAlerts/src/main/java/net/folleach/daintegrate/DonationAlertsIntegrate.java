package net.folleach.daintegrate;

import net.folleach.daintegrate.listeners.IListener;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class DonationAlertsIntegrate {
    public static final DonationAlertsIntegrate Instance = new DonationAlertsIntegrate();

    private ConcurrentHashMap<String, PropertiesDescriptor> handlers = new ConcurrentHashMap<>();
    private ArrayList<IListener<ReadOnlyDonationAlertsEvent>> listeners = new ArrayList<>();

    public DonationAlertsIntegrate()
    {
    }

    public static IModuleConfiguration configure(String modId, String modUrl) {
        return new ModuleConfiguration(modId, modUrl);
    }

    static <T extends IHandler> void registerHandler(T handler, String modId, String modUrl) {
        var descriptor = new PropertiesDescriptor<T>(handler, new EntityId(modId, handler.getImplementationId(), "handler", modUrl));
        Instance.handlers.put(descriptor.getUniqueId(), descriptor);
    }

    static void registerEventListener(IListener<ReadOnlyDonationAlertsEvent> listener) {
        Instance.listeners.add(listener);
    }

    public static Iterator<PropertiesDescriptor> getHandlers() {
        return Instance.handlers.elements().asIterator();
    }

    public static Iterator<IListener<ReadOnlyDonationAlertsEvent>> getEventListeners() {
        return Instance.listeners.iterator();
    }
}
