package ru.sbsoft.system.dao.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author vk
 */
public class PolymorphTypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private static final String CLASSNAME = "__CLASSNAME";
    private static final String INSTANCE = "__INSTANCE";

    private final EnumJsonAdapter enumAdapter = new EnumJsonAdapter();

    @Override
    public JsonElement serialize(T t, Type type, JsonSerializationContext jsc) {
        JsonObject res = new JsonObject();
        String className = t.getClass().getName();
        res.addProperty(CLASSNAME, className);
        JsonElement elem = jsc.serialize(t);
        res.add(INSTANCE, elem);
        return res;
    }

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jsonObject = je.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        String className = prim.getAsString();
        try {
            Class<T> klass = (Class<T>) Class.forName(className);
            return jdc.deserialize(jsonObject.get(INSTANCE), klass);
        } catch (ClassNotFoundException ex) {
            throw new JsonParseException(ex.getMessage(), ex);
        }
    }
}
