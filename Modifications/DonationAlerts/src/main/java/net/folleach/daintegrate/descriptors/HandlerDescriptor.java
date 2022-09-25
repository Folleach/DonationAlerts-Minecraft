package net.folleach.daintegrate.descriptors;

import net.folleach.daintegrate.IHandler;

public class HandlerDescriptor<T> {
    private final IHandler<T> handler;
    private final PropertiesDescriptor<T> properties;

    public HandlerDescriptor(IHandler<T> handler, PropertiesDescriptor<T> propertiesDescriptor) {
        this.handler = handler;
        this.properties = propertiesDescriptor;
    }

    public IHandler<T> getHandler() {
        return handler;
    }

    public PropertiesDescriptor<T> getProperties() {
        return properties;
    }
}
