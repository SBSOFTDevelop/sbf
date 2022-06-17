/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.core.client;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.core.client.BindingPropertySet.PropertyName;

/**
 * Configures Sbsoft SVC client side logging controlled by the svc.logging.enabled
 * property.
 */
public class SVCLogConfiguration {

  @PropertyName("svc.logging.enabled")
  interface LogConfiguration extends BindingPropertySet {
    @PropertyValue("true")
    boolean loggingIsEnabled();
  }

  /**
   * Returns true if SVC framework logging is enabled.
   * 
   * @return true if enabled
   */
  public static boolean loggingIsEnabled() {
    return impl.loggingIsEnabled();
  }

  private static LogConfiguration impl = GWT.create(LogConfiguration.class);
}
