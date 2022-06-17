/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.info;

import com.google.gwt.safehtml.shared.SafeHtml;
import ru.sbsoft.svc.core.client.XTemplates;

public class TemplateInfoConfig<T> extends InfoConfig {

  interface DataRenderer<T> extends XTemplates {
    SafeHtml render(T data);
  }

  private DataRenderer<T> renderer;
  private T data;

  public TemplateInfoConfig(DataRenderer<T> renderer) {
    this.renderer = renderer;
  }

  public DataRenderer<T> getRenderer() {
    return renderer;
  }

  public void setRenderer(DataRenderer<T> renderer) {
    this.renderer = renderer;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  protected SafeHtml render(Info info) {
    return renderer.render(data);
  }

}
