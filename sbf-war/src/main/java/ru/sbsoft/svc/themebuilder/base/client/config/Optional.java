/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.themebuilder.base.client.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for any optional ThemeDetail configs.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Optional {

  /**
   * Default value used.  Must be convertible to return type
   *
   * @return default value
   */
  String defaultValue();

  /**
   * comment for skeleton generation
   *
   * @return comment
   */
  String comment();
}
