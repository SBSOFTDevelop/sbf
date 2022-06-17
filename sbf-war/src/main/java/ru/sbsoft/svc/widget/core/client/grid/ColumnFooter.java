/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */

package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.dom.DomHelper;
import ru.sbsoft.svc.core.client.dom.XDOM;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridStyles;
import ru.sbsoft.svc.widget.core.client.grid.GridView.GridTemplates;

/**
 * Column footer widget for <code>Grid</code>, which renders one to many aggregation rows.
 */
public class ColumnFooter<M> extends Component {

  protected ColumnModel<M> cm;
  protected Grid<M> grid;
  protected GridView<M> gridView;

  private GridStyles styles;
  private GridTemplates tpls;
  private XElement table;

  /**
   * Creates a new column footer.
   * 
   * @param grid the target grid
   * @param cm the column model
   */
  public ColumnFooter(Grid<M> grid, ColumnModel<M> cm) {
    this.grid = grid;
    this.cm = cm;

    this.gridView = grid.getView();
    this.tpls = gridView.tpls;

    this.styles = grid.getView().getAppearance().styles();

    setElement(Document.get().createDivElement());
    setStyleName(styles.footer());
    getElement().getStyle().setOverflow(Overflow.HIDDEN);

    SafeStyles rowStyles = XDOM.EMPTY_SAFE_STYLE;

    getElement().setInnerSafeHtml(
        tpls.table("", rowStyles, SafeHtmlUtils.EMPTY_SAFE_HTML,
            gridView.renderHiddenHeaders(gridView.getColumnWidths())));

    table = getElement().getFirstChildElement().cast();

    DomHelper.append(table.getFirstChildElement().getNextSiblingElement(), renderRows());
  }

  /**
   * Updates the column hidden state.
   * 
   * @param column the target column
   * @param hidden true if hidden
   */
  public void updateColumnHidden(int column, boolean hidden) {
    adjustColumnWidths();
  }

  /**
   * Updates the column width.
   * 
   * @param column the target column
   * @param width the new width
   */
  public void updateColumnWidth(int column, int width) {
    adjustColumnWidths();
  }

  /**
   * Updates the total column width.
   * 
   * @param offset the offset
   * @param width the total width
   */
  public void updateTotalWidth(int offset, int width) {
    table.getStyle().setWidth(width, Unit.PX);
  }

  protected SafeHtml doRender() {
    int colCount = cm.getColumnCount();

    // root builder
    SafeHtmlBuilder buf = new SafeHtmlBuilder();

    int rows = cm.getAggregationRows().size();

    String cellInner = styles.cellInner() + " " + gridView.getStateStyles().cellInner();

    SafeStyles empty = XDOM.EMPTY_SAFE_STYLE;

    for (int j = 0; j < rows; j++) {
      AggregationRowConfig<M> config = cm.getAggregationRow(j);

      SafeHtmlBuilder trBuilder = new SafeHtmlBuilder();

      // loop each cell per row
      for (int i = 0; i < colCount; i++) {
        String cellClass = styles.cell() + " " + gridView.getStateStyles().cell();
        String cs = config.getCellStyle(cm.getColumn(i));
        if (cs != null) {
          cellClass += " " + cs;
        }
        HorizontalAlignmentConstant align = cm.getColumnHorizontalAlignment(i);
        SafeStyles s = empty;
        if (align != null) {
          s = SafeStylesUtils.fromTrustedString("text-align:" + align.getTextAlignString() + ";");
        }
        trBuilder.append(tpls.td(i, cellClass, empty, cellInner, s, getRenderedValue(j, i)));
      }
      buf.append(tpls.tr("", trBuilder.toSafeHtml()));
    }

    return buf.toSafeHtml();
  }

  @Override
  protected void onAfterFirstAttach() {
    refresh();
    super.onAfterFirstAttach();
  }

  protected void refresh() {
    XElement tbody = table.getFirstChildElement().getNextSiblingElement().<XElement> cast();
    tbody.removeChildren();
    DomHelper.append(tbody, renderRows());
  }

  protected <N, O> SafeHtml getRenderedValue(int row, int col) {
    AggregationRowConfig<M> config = cm.getAggregationRow(row);
    ColumnConfig<M, N> c = cm.getColumn(col);

    AggregationRenderer<M> renderer = config.getRenderer(c);
    if (renderer != null) {
      SafeHtml s = renderer.render(col, grid);
      return s;
    }
    return SafeHtmlUtils.EMPTY_SAFE_HTML;
  }

  protected SafeHtml renderRows() {
    return doRender();
  }

  private void adjustColumnWidths() {
    int[] columnWidths = gridView.getColumnWidths();

    table.getStyle().setWidth(gridView.getTotalWidth(), Unit.PX);

    NodeList<Element> ths = table.getFirstChildElement().getFirstChildElement().getChildNodes().cast();
    for (int i = 0; i < ths.getLength(); i++) {
      ths.getItem(i).getStyle().setPropertyPx("width", cm.isHidden(i) ? 0 : columnWidths[i]);
    }

    // column widths wrong on column hide / show
    if (SVC.isWebKit()) {
      table.getStyle().setProperty("display", "block");

      Scheduler.get().scheduleFinally(new ScheduledCommand() {

        @Override
        public void execute() {
          table.getStyle().clearDisplay();
        }
      });
    }
  }
}
