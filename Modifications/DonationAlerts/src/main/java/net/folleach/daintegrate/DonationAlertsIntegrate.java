package net.folleach.daintegrate;

import net.folleach.daintegrate.configurations.TriggerDto;
import net.folleach.daintegrate.descriptors.HandlerDescriptor;
import net.folleach.daintegrate.descriptors.PropertiesDescriptor;
import net.folleach.daintegrate.descriptors.SensitiveDescriptor;
import net.folleach.daintegrate.listeners.IListener;
import net.folleach.daintegrate.sensitives.ISensitive;
import net.folleach.dontaionalerts.ReadOnlyDonationAlertsEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class DonationAlertsIntegrate {
    public static final DonationAlertsIntegrate Instance = new DonationAlertsIntegrate();

    private ConcurrentHashMap<String, PropertiesDescriptor> entities = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, HandlerDescriptor> handlers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, SensitiveDescriptor> sensitives = new ConcurrentHashMap<>();
    private TriggerDto[] triggers;
    private ArrayList<IListener<ReadOnlyDonationAlertsEvent>> listeners = new ArrayList<>();

    public DonationAlertsIntegrate()
    {
    }

    public static IModuleConfiguration configure(String modId, String modUrl) {
        return new ModuleConfiguration(modId, modUrl);
    }

    static <T extends IHandler> void registerHandler(T handler, String modId, String modUrl) {
        var descriptor = new PropertiesDescriptor<T>(handler, new EntityId(modId, handler.getImplementationId(), "handler", modUrl));
        Instance.entities.put(descriptor.getUniqueId(), descriptor);
        Instance.handlers.put(descriptor.getUniqueId(), new HandlerDescriptor(handler, descriptor));
    }

    static <T extends ISensitive> void registerSensitive(T sensitive, String modId, String modUrl) {
        var descriptor = new PropertiesDescriptor<T>(sensitive, new EntityId(modId, sensitive.getImplementationId(), "sensitive", modUrl));
        Instance.entities.put(descriptor.getUniqueId(), descriptor);
        Instance.sensitives.put(descriptor.getUniqueId(), new SensitiveDescriptor(sensitive, descriptor));
    }

    static void registerEventListener(IListener<ReadOnlyDonationAlertsEvent> listener) {
        Instance.listeners.add(listener);
    }

    public static Iterator<HandlerDescriptor> getHandler() {
        return Instance.handlers.elements().asIterator();
    }

    public static Iterator<IListener<ReadOnlyDonationAlertsEvent>> getEventListeners() {
        return Instance.listeners.iterator();
    }

    public static boolean knownEntity(String id) {
        return Instance.entities.containsKey(id);
    }

    public static PropertiesDescriptor getProperties(String id) {
        return Instance.entities.get(id);
    }

    public static SensitiveDescriptor getSensitive(String id) {
        return Instance.sensitives.get(id);
    }

    public static HandlerDescriptor getHandler(String id) {
        return Instance.handlers.get(id);
    }

    public static Stream<TriggerDto> getTriggers() {
        return Arrays.stream(Instance.triggers);
    }

    void updateTriggers(TriggerDto[] triggers) {
        this.triggers = triggers;
    }
}
