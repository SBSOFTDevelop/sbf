package ru.sbsoft.system.dao.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class AbstractClassJsonAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

    private static final String CLASSNAME = "__CLASSNAME";

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(src, src.getClass());
        jsonElement.getAsJsonObject().addProperty(CLASSNAME, src.getClass().getCanonicalName());
        return jsonElement;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String className = jsonObj.get(CLASSNAME).getAsString();
        try {
            Class<?> klass = Class.forName(className);
            return context.deserialize(json, klass);
        } catch (ClassNotFoundException ex) {
            throw new JsonParseException(ex);
        }
    }

}
