/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.dnd.core.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.widget.core.client.Component;

/**
 * A custom widget used to show insert locations with drop targets.
 */
public class Insert extends Component {

  public static class DefaultInsertAppearance implements InsertAppearance {

    public interface InsertResources extends ClientBundle {

      ImageResource left();

      @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
      ImageResource mid();

      ImageResource right();

      @Source("Insert.gss")
      InsertStyle style();

    }

    public interface InsertStyle extends CssResource {

      String bar();

      String left();

      String mid();

      String right();

    }

    public interface Template extends XTemplates {
      @XTemplate(source = "Insert.html")
      SafeHtml render(InsertStyle style);
    }

    private InsertStyle style;
    private Template template;

    public DefaultInsertAppearance() {
      this((InsertResources) GWT.create(InsertResources.class));
    }

    public DefaultInsertAppearance(InsertResources resources) {
      this.style = resources.style();
      this.style.ensureInjected();

      this.template = GWT.create(Template.class);
    }

    @Override
    public void render(SafeHtmlBuilder sb) {
      sb.append(template.render(style));
    }

  }

  public interface InsertAppearance {
    void render(SafeHtmlBuilder sb);
  }

  private final InsertAppearance appearance;
  private static final Insert instance = GWT.create(Insert.class);

  public static Insert get() {
    return instance;
  }

  protected Insert() {
    this(GWT.<InsertAppearance>create(InsertAppearance.class));
  }

  protected Insert(InsertAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));

    setShadow(false);
    hide();
  }

  public InsertAppearance getAppearance() {
    return appearance;
  }

  public void show(Element c) {
    c.insertBefore(getElement(), null);
    show();
  }

  @Override
  protected void onHide() {
    super.onHide();
    getElement().removeFromParent();
  }

  @Override
  protected void onShow() {
    super.onShow();
    if (!getElement().isConnected()) {
      Document.get().getBody().insertBefore(getElement(), null);
    }
  }

}
