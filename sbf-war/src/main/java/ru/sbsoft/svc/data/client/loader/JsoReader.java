/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.data.client.loader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.web.bindery.autobean.gwt.client.impl.JsoSplittable;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import ru.sbsoft.svc.data.shared.loader.AbstractAutoBeanReader;
import ru.sbsoft.svc.data.shared.loader.DataReader;
import ru.sbsoft.svc.data.shared.loader.Loader;

/**
 * Simple {@link DataReader} implementation to turn JSOs into AutoBeans
 * automatically.
 * 
 * @param <M> Expected model type by the {@link Loader} that uses this
 *          {@link DataReader}
 * @param <Base> Intermediate type before getting to M, allowing
 *          {@link #createReturnData(Object, Object)} to further modify the data
 */
public class JsoReader<M, Base> extends AbstractAutoBeanReader<M, Base, JavaScriptObject> {

  /**
   * Creates a new JSO reader that can turn a JSO into an AutoBean.
   * 
   * @param factory an auto bean factory capable of encoding objects of type M
   * @param rootBeanType AutoBean based type to represent the base data
   */
  public JsoReader(AutoBeanFactory factory, Class<Base> rootBeanType) {
    super(factory, rootBeanType);
  }

  @Override
  protected Splittable readSplittable(Object loadConfig, JavaScriptObject data) {
    if (GWT.isScript()) {
      return data.<JsoSplittable> cast();
    } else {
      JSONObject json = new JSONObject(data);
      return StringQuoter.split(json.toString());
    }
  }

}
