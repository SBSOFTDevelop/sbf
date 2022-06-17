/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.shared;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.core.java.util.Collection_CustomFieldSerializerBase;

public class FastSet_CustomFieldSerializer {
  public static void deserialize(SerializationStreamReader streamReader, FastSet instance)
      throws SerializationException {
    Collection_CustomFieldSerializerBase.deserialize(streamReader, instance);
  }

  public static void serialize(SerializationStreamWriter streamWriter, FastSet instance) throws SerializationException {
    Collection_CustomFieldSerializerBase.serialize(streamWriter, instance);
  }
}
