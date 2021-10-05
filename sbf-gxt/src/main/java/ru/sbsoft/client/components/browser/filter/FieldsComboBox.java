package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.FieldAccess;
import ru.sbsoft.client.components.browser.filter.fields.FieldHolder;
import ru.sbsoft.client.components.browser.filter.fields.ItemHolder;
import ru.sbsoft.client.components.form.ComboBox;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.utils.ClientUtils;

public class FieldsComboBox extends ComboBox<CustomHolder> {

    public FieldsComboBox(ListStore<CustomHolder> store, ColumnSource columnSource) {
        super(new FieldsComboBoxCell(store, new FieldAccess(), columnSource));

        setSelectImmediately(true);
        setEditable(true);
        setAllowBlank(true);
        setAutoValidate(false);
        setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        setWidth(250);
        setMinListWidth(isComplexStructure(store) ? 500 : 300);

        addBeforeSelectionHandler(new BeforeSelectionHandler<CustomHolder>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<CustomHolder> event) {
                CustomHolder h = event.getItem();
                if (h == null || !ClientUtils.isAssignableFrom(ItemHolder.class, h.getClass())) {
                    event.cancel();
                }
            }
        });
    }

    private boolean isComplexStructure(ListStore<CustomHolder> store) {
        for (int i = 0, size = store.size(); i < size; i++) {
            CustomHolder h = store.get(i);
            if (h instanceof FieldHolder) {
                if (((FieldHolder) h).getColumn().getColumn().getGroup() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static interface ColumnSource {

        public CustomColumnConfig getColumn();

    }

    public static class FieldsComboBoxCell extends ComboBoxCell<CustomHolder> {

        private final ColumnSource columnSource;
        private final String UNPOSSIBLE_QUERY = "[eq, gbplf, crjdjhjlf - yfif cbkf l;buehlf";

        public FieldsComboBoxCell(ListStore<CustomHolder> store, FieldAccess fieldAccess, ColumnSource columnSource) {
            super(store, fieldAccess, fieldAccess);
            this.columnSource = columnSource;
        }

        @Override
        protected boolean itemMatchesQuery(CustomHolder item, String query) {
            return item.mathes(query, columnSource == null ? null : columnSource.getColumn().getColumn());
        }

        @Override
        public void doQuery(Context context, XElement parent, ValueUpdater<CustomHolder> updater, CustomHolder value, String query, boolean force) {
            if (columnSource != null) {
                lastQuery = UNPOSSIBLE_QUERY;
            }
            super.doQuery(context, parent, updater, value, query, force);
        }

        @Override
        protected void onResultsLoad(Context context, XElement parent, ValueUpdater<CustomHolder> updater, CustomHolder value) {
            super.onResultsLoad(context, parent, updater, value);
            select(value);
        }
    }
}
