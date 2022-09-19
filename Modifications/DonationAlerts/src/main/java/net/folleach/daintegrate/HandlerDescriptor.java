package net.folleach.daintegrate;

public class HandlerDescriptor<T> {
    private final IHandler<T> handler;
    private final PropertiesDescriptor<T> propertiesDescriptor;

    public HandlerDescriptor(IHandler<T> handler, PropertiesDescriptor<T> propertiesDescriptor) {
        this.handler = handler;
        this.propertiesDescriptor = propertiesDescriptor;
    }
}
