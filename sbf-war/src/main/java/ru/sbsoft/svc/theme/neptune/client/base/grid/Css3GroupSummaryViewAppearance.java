/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.theme.neptune.client.base.grid;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.resources.client.CssResource.Import;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStateStyles;
import ru.sbsoft.svc.widget.core.client.grid.GroupSummaryView.GroupSummaryViewAppearance;
import ru.sbsoft.svc.widget.core.client.grid.GroupSummaryView.GroupSummaryViewStyle;

public class Css3GroupSummaryViewAppearance extends Css3GroupingViewAppearance implements GroupSummaryViewAppearance {

  public interface Css3GroupSummaryResources extends Css3GroupingViewResources {
    @Override
    @Import(GridStateStyles.class)
    @Source({"Css3GroupingView.gss", "Css3GroupSummaryView.gss"})
    Css3GroupSummaryStyles style();
  }

  public interface Css3GroupSummaryStyles extends Css3GroupingViewStyle, GroupSummaryViewStyle {
    String hideSummaries();
  }

  public Css3GroupSummaryViewAppearance() {
    this(GWT.<Css3GroupSummaryResources>create(Css3GroupSummaryResources.class));
  }

  public Css3GroupSummaryViewAppearance(Css3GroupSummaryResources resources) {
    super(resources);
  }

  @Override
  public void toggleSummaries(XElement parent, boolean visible) {
    parent.setClassName(style().hideSummaries(), !visible);
  }

  @Override
  public NodeList<Element> getSummaries(XElement table) {
    return table.select("." + style().summaryRow());
  }

  @Override
  public int getGroupIndex(XElement group) {
    return group.getParentElement().<XElement>cast().indexOf(group) / 3;
  }

  @Override
  public Css3GroupSummaryStyles style() {
    return (Css3GroupSummaryStyles) super.style();
  }
}
