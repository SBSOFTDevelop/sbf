package ru.sbsoft.client.components.grid;

import com.google.gwt.core.client.Duration;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.Header;
import ru.sbsoft.svc.widget.core.client.event.LiveGridViewUpdateEvent;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import java.util.Objects;
import java.util.logging.Logger;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Добавляет к указанному компоненту информацию по таблице: порядеовый номер
 * выделенной строки, количество отмеченных строк, количество отображенных на
 * странице строк.
 *
 * @author balandin
 * @since May 8, 2014 8:11:13 PM
 */
public class GridInfoPlugin {

    private GridInfoPlugin() {
    }

    public static <M extends MarkModel> void bind(BaseGrid<M> grid, final GridMenu menu) {
        new MenuInfoSupport(grid, menu);
    }

    public static <M extends MarkModel> void bind(BaseGrid<M> grid, final ContentPanel contentPanel) {
        new ContentPanelInfoSupport(grid, contentPanel);
    }

    private abstract static class CustomInfoSupport<M extends MarkModel> extends AbstractAction {

        protected final BaseGrid<M> baseGrid;
        protected final Grid grid;
        private boolean infoUpdateScheduled = false;
        private static final Logger logger = Logger.getLogger(CustomInfoSupport.class.getName());

        private void _log(String log) {

            if (SVCLogConfiguration.loggingIsEnabled()) {
                logger.finest(log);
            }

        }

        public CustomInfoSupport(BaseGrid<M> _baseGrid) {
            Objects.requireNonNull(_baseGrid, "Grid is required for " + this.getClass().getName());
            this.baseGrid = _baseGrid;
            this.grid = _baseGrid.getGrid();

            _baseGrid.getActionManager().registerAction(this);
            _baseGrid.getGrid().getView().addLiveGridViewUpdateHandler(new LiveGridViewUpdateEvent.LiveGridViewUpdateHandler() {
                @Override
                public void onUpdate(LiveGridViewUpdateEvent event) {
                    scheduleInfoUpdate();
                }
            });
        }

        public String makeInfoLine() {
            SBLiveGridView<M> view = baseGrid.getGrid().getView();
            int totalCount = view.getTotalCount();
            int viewIndex = GridHelper.getTopVisibleRowIndex(grid);
            int lastViewIndex = GridHelper.getBottomVisibleRowIndex(grid, totalCount);
            int marks = baseGrid.getTotalMarkedRecordsCount();
            
            int selected = -1;
            final M rec = baseGrid.getGrid().getSelectionModel().getSelectedItem();
            if (rec != null) {
                view.getLiveStoreOffset();
                int n = view.getCacheStore().indexOf(rec);
                if (n != -1) {
                    selected = view.getLiveStoreOffset() + n + 1;
                }
            }

            StringBuilder b = new StringBuilder();

            if (selected != -1) {
                b.append(img(SBFResources.EDITOR_ICONS.NewLine16(), I18n.get(SBFBrowserStr.labelNoCurrentRecord)));
                b.append("&nbsp;&nbsp;");

                b.append(selected);
                b.append("&nbsp;&nbsp;");
            }

            if (marks > 0) {
                b.append(img(SBFResources.BROWSER_ICONS.Pin16(), I18n.get(SBFBrowserStr.labelMarkedRecords)));
                b.append("&nbsp;&nbsp;");

                b.append(marks);
                b.append("&nbsp;&nbsp;");
            }

            b.append(img(SBFResources.BROWSER_ICONS.Row16(), I18n.get(SBFBrowserStr.labelTotalRecords)));
            b.append("&nbsp;&nbsp;");

            b.append(viewIndex >= 0 ? viewIndex + 1 : 0);
            b.append("&nbsp;-&nbsp;");
            b.append(lastViewIndex >= 0 ? Math.min(totalCount, lastViewIndex + 1) : 0);
            b.append("&nbsp;/&nbsp;");
            b.append(totalCount);

            return b.toString();
        }

        private String img(ImageResource res, String title) {
            Image i = new Image(res);
            i.setTitle(title);
            i.getElement().getStyle().setPosition(Style.Position.RELATIVE);
            i.getElement().getStyle().setTop(4, Style.Unit.PX);
            i.getElement().getStyle().setHeight(16, Style.Unit.PX);
            i.getElement().getStyle().setWidth(16, Style.Unit.PX);
            return i.toString();
        }

        @Override
        public boolean checkEnabled() {
            scheduleInfoUpdate();
            return true;
        }

        private void scheduleInfoUpdate() {
            if (!infoUpdateScheduled) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    private final Duration timer = new Duration();

                    @Override
                    public void execute() {
                        if (timer.elapsedMillis() >= 100) {
                            infoUpdateScheduled = false;
                            updateInfoLine();
                        } else {
                            Scheduler.get().scheduleDeferred(this);
                        }
                    }
                });
                infoUpdateScheduled = true;
            }
        }

        protected abstract void updateInfoLine();

        @Override
        protected void onExecute() {
        }
    }

    private static class ContentPanelInfoSupport extends CustomInfoSupport {

        private final ContentPanel panel;

        public ContentPanelInfoSupport(BaseGrid baseGrid, ContentPanel panel) {
            super(baseGrid);
            this.panel = panel;
        }

        @Override
        protected void updateInfoLine() {
            Header h = panel.getHeader();
            XElement div = (XElement) h.getData("d");
            if (div == null) {
                XElement caption = h.getElement().getChild(2).cast();
                caption.getStyle().setFloat(Style.Float.LEFT);

                div = DOM.createDiv().cast();
                div.getStyle().setFloat(Style.Float.RIGHT);
                div.getStyle().setDisplay(Style.Display.INLINE);
                div.getStyle().setFontSize(12, Style.Unit.PX);
                div.getStyle().setFontWeight(Style.FontWeight.NORMAL);
                div.getStyle().setProperty("fontFamily", "arial,verdana,sans-serif");
                div.getStyle().setMarginTop(-3, Style.Unit.PX);
                div.getStyle().setMarginRight(5, Style.Unit.PX);
                div.getStyle().setColor("#000");

                h.getElement().appendChild(div);
                h.setData("d", div);
            }
            div.removeChildren();
            div.appendChild(new HTML(SafeHtmlUtils.fromTrustedString(makeInfoLine())).getElement());
        }
    }

    private static class MenuInfoSupport extends CustomInfoSupport {

        private final GridMenu menu;

        public MenuInfoSupport(BaseGrid baseGrid, GridMenu menu) {
            super(baseGrid);
            this.menu = menu;
        }

        @Override
        protected void updateInfoLine() {
            SafeHtmlBuilder b = new SafeHtmlBuilder();
            b.appendHtmlConstant("<span style='font: 12px arial,verdana,sans-serif;'>" + makeInfoLine() + "</span>");

            XElement div = (XElement) menu.getData("d");
            div.removeChildren();
            div.appendChild(new HTML(b.toSafeHtml()).getElement());
            menu.getElement().appendChild(div);
        }
    }
}
