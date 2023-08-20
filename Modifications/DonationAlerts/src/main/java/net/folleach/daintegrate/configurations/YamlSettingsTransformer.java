package net.folleach.daintegrate.configurations;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.folleach.daintegrate.DonationAlertsIntegrate;
import net.folleach.daintegrate.ITransformer;
import org.yaml.snakeyaml.Yaml;

import java.util.LinkedHashMap;

public class YamlSettingsTransformer implements ITransformer<String, SettingsDto> {
    private static final Gson gson = new Gson();

    @Override
    public SettingsDto transform(String input) {
        var yaml = new Yaml();
        var result = yaml.loadAs(input, SettingsDto.class);
        if (result.triggers == null)
            return result;
        for (var trigger : result.triggers) {
            if (trigger == null)
                continue;
            for (var handler : trigger.handlers) {
                if (handler == null)
                    continue;
                replaceHashMap(handler.properties);
            }
            for (var sensitive : trigger.sensitives) {
                if (sensitive == null)
                    continue;
                replaceHashMap(sensitive.properties);
            }
        }
        return null;
    }

    // (ง ◉ _ ◉)ง Even the UI in the first versions of the mod will not be worse than this
    @SuppressWarnings("unchecked")
    private void replaceHashMap(PropertiesDto properties) {
        var typeName = properties.type;
        if (!DonationAlertsIntegrate.knownEntity(typeName))
            throw new JsonParseException("Does not contains '" + typeName + "' properties descriptor");
        var map = (LinkedHashMap<String, Object>)properties.value;
        var type = DonationAlertsIntegrate.getProperties(typeName).getType();
        var json = gson.toJson(map);
        properties.value = gson.fromJson(json, type);
    }
}
