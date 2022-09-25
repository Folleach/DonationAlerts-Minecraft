package net.folleach.daintegrate.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.folleach.daintegrate.ITransformer;
import net.folleach.daintegrate.PropertyDeserializer;

public class SettingsDtoTransformer implements ITransformer<String, SettingsDto> {
    private final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(IProperties.class, new PropertyDeserializer())
            .create();

    @Override
    public SettingsDto transform(String input) {
        return gson.fromJson(input, SettingsDto.class);
    }
}
