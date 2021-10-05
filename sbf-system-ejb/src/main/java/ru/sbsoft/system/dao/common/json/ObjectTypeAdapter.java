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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.interfaces.IDynamicGridType;

/**
 *
 * @author vk
 */
public class ObjectTypeAdapter implements JsonSerializer<ObjectType>, JsonDeserializer<ObjectType> {

    private static final String TYPENAME = "__TYPENAME";
    private static final String TYPEBODY = "__TYPEBODY";

    private final List<SerializerInfo<?>> ser = new ArrayList<>();
    private final Map<String, JsonDeserializer<?>> des = new HashMap<>();
    
    private final EnumJsonAdapter enumJsonAdapter;

    public ObjectTypeAdapter() {
        register("TYPEENUM", Enum.class, enumJsonAdapter = new EnumJsonAdapter());
        register("TYPEDYNAMIC", IDynamicGridType.class, new DynamicGridTypeAdapter());
    }

    @Override
    public JsonElement serialize(ObjectType t, Type type, JsonSerializationContext jsc) {
        JsonObject res = new JsonObject();
        SerializerInfo s = getSerializerInfo(t.getClass());
        res.addProperty(TYPENAME, s.getTypeName());
        res.add(TYPEBODY, s.getSerializer().serialize(t, t.getClass(), jsc));
        return res;
    }

    @Override
    public ObjectType deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jo = je.getAsJsonObject();
        JsonPrimitive typeNameJson = jo.getAsJsonPrimitive(TYPENAME);
        if (typeNameJson != null) {
            String typeName = typeNameJson.getAsString();
            JsonDeserializer d = des.get(typeName);
            if (d == null) {
                throw new JsonParseException("Deserializer not found for type name: " + typeName);
            }
            return (ObjectType) d.deserialize(jo.get(TYPEBODY), type, jdc);
        }else{
            // suppose this is just enum as was before
            return (ObjectType) enumJsonAdapter.deserialize(je, type, jdc);
        }
    }

    private <T, A extends JsonSerializer<T> & JsonDeserializer<T>> void register(String typename, Class<T> klass, A conv) {
        ser.add(new SerializerInfo<>(typename, klass, conv));
        des.put(typename, conv);
    }

    private SerializerInfo getSerializerInfo(Class c) {
        for (SerializerInfo inf : ser) {
            if (inf.getKlass().isAssignableFrom(c)) {
                return inf;
            }
        }
        throw new JsonParseException("Unsupported ObjectType: " + c.getName() + ". Supported: " + ser.stream().map(inf -> inf.getClass().getName()).collect(Collectors.toList()));
    }

    private static class SerializerInfo<T> {

        private final String typeName;
        private final JsonSerializer<T> serializer;
        private final Class<T> klass;

        public SerializerInfo(String typeName, Class<T> klass, JsonSerializer<T> serializer) {
            this.typeName = typeName;
            this.serializer = serializer;
            this.klass = klass;
        }

        public String getTypeName() {
            return typeName;
        }

        public JsonSerializer<T> getSerializer() {
            return serializer;
        }

        public Class<T> getKlass() {
            return klass;
        }
    }
}
