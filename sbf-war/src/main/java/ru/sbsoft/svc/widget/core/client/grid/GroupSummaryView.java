/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.data.shared.ListStore;

public class GroupSummaryView<M> extends GroupingView<M> {

  public interface GroupSummaryViewAppearance extends GroupingViewAppearance {
    void toggleSummaries(XElement parent, boolean visible);

    NodeList<Element> getSummaries(XElement table);

    int getGroupIndex(XElement group);

    @Override
    GroupSummaryViewStyle style();
  }

  public interface GroupSummaryViewStyle extends GroupingViewStyle {

    String summaryRow();
  }

  private boolean summaryVisible = true;

  public GroupSummaryView() {
    this(GWT.<GroupSummaryViewAppearance>create(GroupSummaryViewAppearance.class));
  }

  public GroupSummaryView(GridAppearance appearance, GroupSummaryViewAppearance groupingAppearance) {
    super(appearance, groupingAppearance);
  }

  public GroupSummaryView(GroupSummaryViewAppearance groupAppearance) {
    super(groupAppearance);
  }

  @Override
  public GroupSummaryViewAppearance getGroupingAppearance() {
    return (GroupSummaryViewAppearance) super.getGroupingAppearance();
  }

  @Override
  protected int getGroupIndex(XElement group) {
    return group.getParentElement().<XElement>cast().indexOf(group) / 3;
  }

  /**
   * Gets all summary nodes currently rendered in the grid
   * 
   * @return all summary nodes
   */
  public NodeList<Element> getSummaries() {
    return getGroupingAppearance().getSummaries(dataTable);
  }

  /**
   * Returns true if summaries are visible.
   * 
   * @return true for visible
   */
  public boolean isSummaryVisible() {
    return summaryVisible;
  }

  /**
   * Toggles the summary information visibility.
   * 
   * @param visible true for visible, false to hide
   */
  public void toggleSummaries(boolean visible) {
    summaryVisible = visible;
    getGroupingAppearance().toggleSummaries(grid.getElement(), visible);
  }

  protected Map<ValueProvider<? super M, ?>, Number> calculate(List<M> models) {
    Map<ValueProvider<? super M, ?>, Number> data = new HashMap<ValueProvider<? super M, ?>, Number>();

    for (int i = 0, len = cm.getColumnCount(); i < len; i++) {
      SummaryColumnConfig<M,?> cf = (SummaryColumnConfig<M,?>) cm.getColumn(i);
      if (cf.getSummaryType() != null) {
        data.put(cf.getValueProvider(), calculate(cf, models));
      }
    }
    return data;
  }
  
  /**
   * Helper to ensure generics are typesafe.
   *
   * @param cf the summary column config.
   * @param models the list of models.
   * @return Number Returns the value for a summary calculation.
   */
  private <N> Number calculate(SummaryColumnConfig<M, N> cf, List<M> models) {
    ValueProvider<? super M, N> vp = cf.getValueProvider();
    SummaryType<N, ?> summaryType = cf.getSummaryType();
    return summaryType.calculate(models, vp);
  }

  @Override
  protected void onRemove(M m, int index, boolean isUpdate) {
    super.onRemove(m, index, isUpdate);
    if (!isUpdate) {
      refreshSummaries();
    }
  }

  @Override
  protected void onUpdate(ListStore<M> store, List<M> models) {
    super.onUpdate(store, models);
    refreshSummaries();
  }

  protected void refreshSummaries() {
    if (groupingColumn == null) {
      return;
    }

    NodeList<Element> groups = getGroups();
    NodeList<Element> summaries = getSummaries();
    List<GroupingData<M>> groupData = createGroupingData();
    for (int i = 0; i < groupData.size(); i++) {
      Element g = groups.getItem(i);
      if (g == null) continue;
      SafeHtml s = renderGroupSummary(groupData.get(i));
      s = tpls.table("", SafeStylesUtils.fromTrustedString(""), s, SafeHtmlUtils.EMPTY_SAFE_HTML);
      Element existing = summaries.getItem(i);
      existing.getParentElement().replaceChild(XDOM.create(s).getLastChild().<Element>cast().getFirstChildElement(), existing);
    }
  }

  @Override
  protected void renderGroup(SafeHtmlBuilder buf, GroupingData<M> g, SafeHtml renderedRows) {
    super.renderGroup(buf, g, renderedRows);
    buf.append(renderGroupSummary(g));
  }

  protected SafeHtml renderGroupSummary(GroupingData<M> groupInfo) {
    Map<ValueProvider<? super M, ?>, Number> data = calculate(groupInfo.getItems());
    return renderSummary(groupInfo, data);
  }

  protected SafeHtml renderSummary(GroupingData<M> groupInfo, Map<ValueProvider<? super M, ?>, Number> data) {
    int colCount = cm.getColumnCount();
    int last = colCount - 1;

    String unselectableClass = " " + unselectable;

    String cellClass = styles.cell() + " " + states.cell();
    String cellInner = styles.cellInner() + " " + states.cellInner();
    String cellFirstClass = " x-grid-cell-first";
    String cellLastClass = " x-grid-cell-last";

    SafeHtmlBuilder trBuilder = new SafeHtmlBuilder();
    
    for (int i = 0, len = colCount; i < len; i++) {
      SummaryColumnConfig<M,?> cf = (SummaryColumnConfig<M,?>) cm.getColumn(i);
      ColumnData cd = getColumnData().get(i);

      String cellClasses = cellClass;
      cellClasses += (i == 0 ? cellFirstClass : (i == last ? cellLastClass : ""));

      if (cf.getCellClassName() != null) {
        cellClasses += " " + cf.getCellClassName();
      }

      Number n = data.get(cm.getValueProvider(i));
      SafeHtml value = SafeHtmlUtils.EMPTY_SAFE_HTML;

      if (cf.getSummaryFormat() != null) {
        if (n != null) {
          value = SafeHtmlUtils.fromString(cf.getSummaryFormat().format(n.doubleValue()));
        }
      } else if (cf.getSummaryRenderer() != null) {
        value = cf.getSummaryRenderer().render(n, data);
      }
      final SafeHtml tdContent;
      if (!selectable && SVC.isIE()) {
        tdContent = tpls.tdUnselectable(i, cellClasses, cd.getStyles(), cellInner, cf.getColumnTextStyle(), value);
      } else {
        tdContent = tpls.td(i, cellClasses, cd.getStyles(), cellInner, cf.getColumnTextStyle(), value);
      }
      trBuilder.append(tdContent);

    }

    String rowClasses = getGroupingAppearance().style().summaryRow();

    if (!selectable) {
      rowClasses += unselectableClass;
    }

    SafeHtml cells = trBuilder.toSafeHtml();

    return tpls.tr(rowClasses, cells);
  }
}
