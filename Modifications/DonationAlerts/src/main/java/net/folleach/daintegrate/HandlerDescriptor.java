package net.folleach.daintegrate;


import java.lang.reflect.ParameterizedType;

public class HandlerDescriptor<T> {
    private final Class<T> type;
    private final Property[] properties;

    @SuppressWarnings("unchecked")
    public HandlerDescriptor(IHandler<T> handler) {
        this.type = (Class<T>)((ParameterizedType)handler
                .getClass()
                .getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        var fields = type.getDeclaredFields();
        properties = new Property[fields.length];
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            var property = new Property();
            property.name = field.getName();
            properties[i] = property;
        }
    }
}
