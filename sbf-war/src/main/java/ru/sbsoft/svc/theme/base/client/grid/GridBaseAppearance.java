/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.XTemplates;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.core.client.resources.StyleInjectorHelper;
import ru.sbsoft.svc.core.client.util.Util;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridAppearance;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStateStyles;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStyles;

public abstract class GridBaseAppearance implements GridAppearance {

  public interface GridResources extends ClientBundle {

    @Source("Grid.gss")
    @Import(GridStateStyles.class)
    GridStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource specialColumn();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource specialColumnSelected();

  }

  public interface GridStyle extends GridStyles {
    String scroller();

    String body();
  }

  public interface GridTemplates extends XTemplates {
    @XTemplate(source = "Grid.html")
    SafeHtml render(GridStyle style);
  }

  protected final GridResources resources;
  protected final GridStyle style;
  private GridTemplates templates = GWT.create(GridTemplates.class);

  public GridBaseAppearance(GridResources resources) {
    this.resources = resources;
    this.style = this.resources.css();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    sb.append(templates.render(style));
  }

  @Override
  public GridStyles styles() {
    return style;
  }

  @Override
  public Element findRow(Element elem) {
    if (Element.is(elem)) {
      return elem.<XElement> cast().findParentElement("." + style.row(), -1);
    }
    return null;
  }

  @Override
  public NodeList<Element> getRows(XElement parent) {
    return TableElement.as(parent.getFirstChildElement()).getTBodies().getItem(1).getRows().cast();
  }

  @Override
  public Element findCell(Element elem) {
    if (Element.is(elem)) {
      return elem.<XElement> cast().findParentElement("." + style.cell(), -1);
    }
    return null;
  }

  @Override
  public void onRowOver(Element row, boolean over) {
    row.<XElement> cast().setClassName(style.rowOver(), over);
  }

  @Override
  public void onRowHighlight(Element row, boolean highlight) {
    row.<XElement> cast().setClassName(style.rowHighlight(), highlight);
  }

  @Override
  public void onRowSelect(Element row, boolean select) {
  }

  @Override
  public void onCellSelect(Element cell, boolean select) {
  }

  @Override
  public Element getRowBody(Element row) {
    return TableElement.as(row.getFirstChildElement().getFirstChildElement().getFirstChildElement()).getTBodies().getItem(
        1).getRows().getItem(1).getCells().getItem(0).getFirstChildElement();
  }

  @Override
  public SafeHtml renderEmptyContent(String emptyText) {
    return Util.isEmptyString(emptyText) ? Util.NBSP_SAFE_HTML : SafeHtmlUtils.fromString(emptyText);
  }

}
