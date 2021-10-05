package ru.sbsoft.system.dao.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class EnumJsonAdapter implements JsonSerializer<Enum>, JsonDeserializer<Enum> {

    private static final String ENUM = "__ENUM";
    private static final String NAME = "__NAME";

    @Override
    public JsonElement serialize(Enum object, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();
        String className = object.getClass().getCanonicalName();
        retValue.addProperty(ENUM, className);
        String name = ((Enum) object).name();
        retValue.addProperty(NAME, name);
        return retValue;
    }

    @Override
    public Enum deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            String className = jsonObject.get(ENUM).getAsString();
            Class klass = Class.forName(className);
            return Enum.valueOf(klass, jsonObject.get(NAME).getAsString());
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }
}
