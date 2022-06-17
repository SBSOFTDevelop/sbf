package ru.sbsoft.client.components.grid.sort;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.data.shared.SortInfo;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.fields.HoldersList;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.dlgbase.BaseUnitWindow;
import ru.sbsoft.client.components.grid.dlgbase.Group;
import ru.sbsoft.client.components.grid.dlgbase.plugins.SingleFieldChoice;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Диалог сортировки по нескольким столбцам. Позволяет выбрать столбцы и направление сортировки для них. 
 * @author Kiselev
 */
public class SortWindow extends BaseUnitWindow<SortGroup> {

    private static final Logger log = Logger.getLogger(SortWindow.class.getName());

    private final SystemGrid grid;
    private final HoldersList holdersList;

    public SortWindow(SystemGrid grid) {
        super(new SortGroup());
        DEFAULT_WINDOW_WIDTH = 380;
        getHeader().setIcon(SBFResources.BROWSER_ICONS.multiSortIcon());
        getHeader().setText(I18n.get(SBFBrowserStr.menuMultiSort));

        this.grid = grid;
        this.holdersList = new SortHoldersListFactory().create(grid);
        addStandardActions(ADD_ITEM, DEL);
        addPlugin(new SingleFieldChoice());
    }

    @Override
    protected void apply() {
        if (!rootGroup.validate()) {
            ClientUtils.alertWarning(I18n.get(SBFBrowserStr.menuMultiSort), I18n.get(SBFBrowserStr.msgDataNotFilled));
            return;
        }
        grid.applySort(rootGroup.getSort());
        super.hide();
    }

    public void show(List<? extends SortInfo> sort, final Widget parent) {
        rootGroup.clearValue();
        if (sort != null) {
            Group g = rootGroup;
            for (SortInfo s : sort) {
                final SortItem item = createItem();
                try {
                    item.setSortInfo(s);
                    g.addUnit(item);
                } catch (final Exception ex) {
                    String msg = ex.getMessage();
                    GWT.log(msg);
                    log.log(Level.SEVERE, msg, ex);
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            ClientUtils.alertWarning(ex.getLocalizedMessage());
                        }
                    });
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            item.delete();
                        }
                    });
                }
            }
        }
        super.show(parent);
    }

    @Override
    protected SortItem createItem() {
        return tuneItem(new SortItem(holdersList));
    }
}
