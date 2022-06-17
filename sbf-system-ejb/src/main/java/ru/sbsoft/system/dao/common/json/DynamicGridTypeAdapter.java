package ru.sbsoft.system.dao.common.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.IDynamicGridType;

/**
 *
 * @author vk
 */
public class DynamicGridTypeAdapter implements JsonSerializer<IDynamicGridType>, JsonDeserializer<IDynamicGridType> {

    private enum Field {
        groupCode, id, code, itemName, securityId
    }

    private final PolymorphTypeAdapter<ILocalizedString> polymorphTypeAdapter = new PolymorphTypeAdapter<>();

    @Override
    public JsonElement serialize(IDynamicGridType t, Type type, JsonSerializationContext jsc) {
        JsonObject res = new JsonObject();
        res.addProperty(Field.groupCode.name(), t.getGroupCode());
        res.addProperty(Field.id.name(), t.getId());
        res.addProperty(Field.code.name(), t.getCode());
        res.add(Field.itemName.name(), t.getItemName() != null ? polymorphTypeAdapter.serialize(t.getItemName(), t.getItemName().getClass(), jsc) : null);
        res.addProperty(Field.securityId.name(), t.getSecurityId());
        return res;
    }

    @Override
    public IDynamicGridType deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jo = je.getAsJsonObject();
        String groupCode = jo.getAsJsonPrimitive(Field.groupCode.name()).getAsString();
        BigDecimal id = jo.getAsJsonPrimitive(Field.id.name()).getAsBigDecimal();
        String code = jo.getAsJsonPrimitive(Field.code.name()).getAsString();
        ILocalizedString itemName = polymorphTypeAdapter.deserialize(jo.getAsJsonObject(Field.itemName.name()), ILocalizedString.class, jdc);
        String securityId = jo.getAsJsonPrimitive(Field.securityId.name()).getAsString();
        return new PlainDynamicGridType(groupCode, id, code, itemName, securityId);
    }
}
