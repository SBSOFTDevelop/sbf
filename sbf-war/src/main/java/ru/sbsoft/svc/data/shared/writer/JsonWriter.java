/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.shared.writer;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

/**
 * An AutoBeanWriter that writes an auto bean into Json.
 * 
 * @param <M> the starting data format for the model to be inputed
 */
public class JsonWriter<M> extends AutoBeanWriter<M, String> {

  /**
   * Creates a new JSON writer for auto beans.
   * 
   * @param factory the auto bean factory
   * @param clazz the target class
   */
  public JsonWriter(AutoBeanFactory factory, Class<M> clazz) {
    super(factory, clazz);
  }

  public String write(M model) {
    if (model == null) {
      return "null";
    }
    AutoBean<M> autobean = getAutoBean(model);
    if (autobean == null) {
      throw new RuntimeException(
          "Could not serialize "
              + model.getClass()
              + " using Autobeans, it appears to not be backed by an autobean. You may need to implement your own DataWriter.");
    }
    return AutoBeanCodex.encode(autobean).getPayload();
  }
}
