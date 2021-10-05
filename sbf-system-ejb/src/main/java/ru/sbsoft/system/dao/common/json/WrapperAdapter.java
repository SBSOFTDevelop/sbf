package ru.sbsoft.system.dao.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import ru.sbsoft.shared.meta.Wrapper;

public class WrapperAdapter implements JsonSerializer<Wrapper>, JsonDeserializer<Wrapper> {

    private static final String CLASSNAME = "__CLASSNAME";
    private static final String VALUE = "__VALUE";

    @Override
    public JsonElement serialize(Wrapper src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject retValue = new JsonObject();

        Object value = src.getValue();
        Class valueClass = value == null ? Object.class : value.getClass();
        JsonElement valueElement = context.serialize(value, valueClass);

        retValue.addProperty(CLASSNAME, valueClass.getCanonicalName());
        retValue.add(VALUE, valueElement);

        return retValue;
    }

    @Override
    public Wrapper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = json.getAsJsonObject();
            String className = jsonObject.get(CLASSNAME).getAsString();
            JsonElement valueElement = jsonObject.get(VALUE);

            final Object value = context.deserialize(valueElement, Class.forName(className));
            return new Wrapper(value);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e.getMessage());
        }
    }

}
