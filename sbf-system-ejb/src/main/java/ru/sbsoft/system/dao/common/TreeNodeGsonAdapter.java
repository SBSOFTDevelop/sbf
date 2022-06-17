/*
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.system.dao.common;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.TreeNodeModel;

/**
 *
 * @author sokolov
 */
public class TreeNodeGsonAdapter implements JsonSerializer<TreeNode>, JsonDeserializer<TreeNode> {

    @Override
    public JsonElement serialize(TreeNode t, Type type, JsonSerializationContext jsc) {
        JsonElement jsonElement = jsc.serialize(t, t.getClass());
        return jsonElement.getAsJsonObject();
    }

    @Override
    public TreeNode deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        //обрабатываем элемент с возможным элементом code
        String key = "";
        String code = null;
        String title = "";
        boolean leaf = true;
        JsonObject object = je.getAsJsonObject();
        if (object.has("code")) {
            code = object.get("code").getAsString();
        }
        if (object.has("key")) {
            key = object.get("key").getAsString();
        }
        if (object.has("title")) {
            title = object.get("title").getAsString();
        } else if (object.has("name")) {
            title = object.get("name").getAsString();
        }
        if (object.has("leaf")) {
            leaf = object.get("leaf").getAsBoolean();
        }
        return new TreeNodeModel<>(key, code == null ? title : code + "." + title, leaf);
    }
    
}

