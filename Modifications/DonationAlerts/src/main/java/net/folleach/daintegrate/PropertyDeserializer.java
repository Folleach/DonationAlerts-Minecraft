package net.folleach.daintegrate;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.folleach.daintegrate.configurations.IProperties;
import net.folleach.daintegrate.configurations.PropertiesDto;

import java.lang.reflect.Type;

public class PropertyDeserializer implements JsonDeserializer<IProperties> {
    @Override
    public IProperties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        var obj = json.getAsJsonObject();
        var typeName = obj.get("type").getAsString();
        if (!DonationAlertsIntegrate.knownEntity(typeName))
            throw new JsonParseException("Does not contains '" + typeName + "' properties descriptor");
        var type = DonationAlertsIntegrate.getProperties(typeName).getType();
        var value = context.deserialize(obj.get("value"), type);
        var propertyDto = new PropertiesDto<>();
        propertyDto.type = typeName;
        propertyDto.value = value;
        return propertyDto;
    }
}
