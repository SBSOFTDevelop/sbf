/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.cell.core.client.form;

import java.util.logging.Logger;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.dom.XElement;

public class TextInputCell extends ValueBaseInputCell<String> {

  public interface TextFieldAppearance extends ValueBaseFieldAppearance {
    void onResize(XElement parent, int width, int height);

    void render(SafeHtmlBuilder sb, String type, String value, FieldAppearanceOptions options);
  }

  private static Logger logger = Logger.getLogger(TextInputCell.class.getName());

  /**
   * Constructs a TextInputCell that renders its text without HTML markup.
   */
  public TextInputCell() {
    this(GWT.<TextFieldAppearance> create(TextFieldAppearance.class));
  }

  public TextInputCell(TextFieldAppearance appearance) {
    super(appearance, "change", "keyup");

    setWidth(150);
  }

  @Override
  public void finishEditing(Element parent, String value, Object key, ValueUpdater<String> valueUpdater) {
    if (SVCLogConfiguration.loggingIsEnabled()) {
      logger.finest("finishEditing");
    }

    String newValue = getText(XElement.as(parent));

    // Get the view data.
    FieldViewData vd = getViewData(key);
    if (vd == null) {
      vd = new FieldViewData(value);
      setViewData(key, vd);
    }
    vd.setCurrentValue(newValue);

    boolean change = valueUpdater != null && !vd.getCurrentValue().equals(vd.getLastValue());
    if (SVCLogConfiguration.loggingIsEnabled()) {
      logger.finest("finishEditing value " + (change ? "changed" : "not changed"));
    }

    // Fire the value updater if the value has changed.
    if (change) {
      vd.setLastValue(newValue);
      valueUpdater.update(newValue);
    }

    clearViewData(key);
    clearFocusKey();

    // calling super.finishEditing not needed as programmatic blurs causes issues
  }

  @Override
  public TextFieldAppearance getAppearance() {
    return (TextFieldAppearance) super.getAppearance();
  }

  @Override
  public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
      ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);

    // Ignore events that don't target the input.
    InputElement input = getInputElement(parent);
    Element target = event.getEventTarget().cast();
    if (!input.isOrHasChild(target)) {
      return;
    }

    String eventType = event.getType();
    Object key = context.getKey();
    if ("change".equals(eventType)) {
      if (SVCLogConfiguration.loggingIsEnabled()) {
        logger.finest("onBrowserEvent change event fired");
      }
      finishEditing(parent, value, key, valueUpdater);
    } else if ("keyup".equals(eventType)) {
      // Record keys as they are typed.
      FieldViewData vd = getViewData(key);
      if (vd == null) {
        vd = new FieldViewData(value);
        setViewData(key, vd);
      }
      vd.setCurrentValue(getText(XElement.as(parent)));
    }
  }

  @Override
  public void render(Context context, String value, SafeHtmlBuilder sb) {
    ViewData viewData = checkViewData(context, value);
    String s = (viewData != null) ? viewData.getCurrentValue() : value;

    s = getPropertyEditor().render(s);

    FieldAppearanceOptions options = new FieldAppearanceOptions(getWidth(), getHeight(), isReadOnly(), getEmptyText());
    options.setName(name);
    options.setDisabled(isDisabled());
    getAppearance().render(sb, "text", s == null ? "" : s, options);
  }

  @Override
  public void setSize(XElement parent, int width, int height) {
    super.setSize(parent, width, height);
    getAppearance().onResize(parent, width, height);
  }

  private native void clearFocusKey() /*-{
        this.@com.google.gwt.cell.client.AbstractInputCell::focusedKey = null;
  }-*/;

}
