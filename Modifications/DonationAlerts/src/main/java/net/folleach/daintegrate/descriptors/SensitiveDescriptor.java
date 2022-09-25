package net.folleach.daintegrate.descriptors;

import net.folleach.daintegrate.sensitives.ISensitive;

public class SensitiveDescriptor<T> {
    private final ISensitive<T> sensitive;
    private final PropertiesDescriptor<T> properties;

    public SensitiveDescriptor(ISensitive<T> sensitive, PropertiesDescriptor<T> properties) {
        this.sensitive = sensitive;
        this.properties = properties;
    }

    public ISensitive<T> getSensitive() {
        return sensitive;
    }

    public PropertiesDescriptor<T> getProperties() {
        return properties;
    }
}
