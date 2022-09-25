package net.folleach.daintegrate.descriptors;

import net.folleach.daintegrate.EntityId;
import net.folleach.daintegrate.IHandler;
import net.folleach.daintegrate.Property;
import net.folleach.daintegrate.sensitives.ISensitive;

import java.lang.reflect.ParameterizedType;

public class PropertiesDescriptor<T> {
    private final Class<T> type;
    private final EntityId id;
    private final Property[] properties;

    public PropertiesDescriptor(IHandler<T> handler, EntityId id) {
        this((Object)handler, id);
    }

    public PropertiesDescriptor(ISensitive<T> sensitive, EntityId id) {
        this((Object)sensitive, id);
    }

    private PropertiesDescriptor(Object object, EntityId id) {
        this.type = getType(object);
        this.id = id;
        var fields = type.getDeclaredFields();
        properties = new Property[fields.length];
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            var property = new Property();
            property.name = field.getName();
            properties[i] = property;
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> getType(Object object) {
        return (Class<T>)((ParameterizedType)object
                .getClass()
                .getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
    }

    public String getUniqueId() {
        return id.getUniqueId();
    }

    public Class<T> getType() {
        return type;
    }
}
