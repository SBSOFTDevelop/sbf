package ru.sbsoft.system.dao.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.FilterInfoImpl;

/**
 *
 * @author Sokoloff
 */
public class FilterInfoAdapter implements JsonSerializer<FilterInfo>, JsonDeserializer<FilterInfo> {

    private static final String VALUE1_CLASS = "__VALUE1_CLASS";
    private static final String VALUE2_CLASS = "__VALUE2_CLASS";
    private static final String CLASSNAME = "__CLASSNAME";

    @Override
    public JsonElement serialize(FilterInfo filterInfo, Type type, JsonSerializationContext context) {
        JsonElement jsonElement = context.serialize(filterInfo, filterInfo.getClass());
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        jsonObject.addProperty(CLASSNAME, filterInfo.getClass().getCanonicalName());
        final Object value1 = filterInfo.getValue();
        if (value1 != null) {
            jsonObject.addProperty(VALUE1_CLASS, value1.getClass().getCanonicalName());
        }

        final Object value2 = filterInfo.getSecondValue();
        if (value2 != null) {
            jsonObject.addProperty(VALUE2_CLASS, value2.getClass().getCanonicalName());
        }

        return jsonObject;
    }

    @Override
    public FilterInfo deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String className = jsonObj.get(CLASSNAME).getAsString();
        JsonElement value1Class = jsonObj.get(VALUE1_CLASS);
        JsonElement value2Class = jsonObj.get(VALUE2_CLASS);

        try {
            Class<?> klass = Class.forName(className);
            final FilterInfo filterInfo = context.deserialize(json, klass);
            if (value1Class != null) {
                final JsonElement valueObject = json.getAsJsonObject().get("value");
                Object value = context.deserialize(valueObject, Class.forName(value1Class.getAsString()));
                filterInfo.setValue(value);
            }
            if (value2Class != null) {
                final JsonElement valueObject = json.getAsJsonObject().get("secondValue");
                Object value = context.deserialize(valueObject, Class.forName(value2Class.getAsString()));
                filterInfo.setSecondValue(value);
            }

            return filterInfo;
        } catch (ClassNotFoundException ex) {
            throw new JsonParseException(ex);
        }
    }

}
