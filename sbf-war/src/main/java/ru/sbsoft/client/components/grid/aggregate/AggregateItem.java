package ru.sbsoft.client.components.grid.aggregate;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import java.util.Date;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.FieldHolder;
import ru.sbsoft.client.components.browser.filter.fields.HoldersList;
import ru.sbsoft.client.components.form.ComboBox;
import ru.sbsoft.client.components.grid.aggregate.result.NumberHandler;
import ru.sbsoft.client.components.grid.aggregate.result.TemporalHandler;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.NumericColumnConfig;
import ru.sbsoft.client.components.grid.column.TemporalColumnConfig;
import ru.sbsoft.client.components.grid.dlgbase.HoldersStore;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.aggregate.AggregateDef;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;

/**
 *
 * @author Kiselev
 */
public class AggregateItem extends Item {

    private final ComboBox<IAggregate> operation;
    private final TextField result;

    public AggregateItem(HoldersList holdersList, List<IAggregate> operations) {
        super(new HoldersStore(holdersList));
        operation = new ComboBox<IAggregate>(new LabelProvider<IAggregate>() {
            @Override
            public String getLabel(IAggregate item) {
                return item.getName();
            }
        });
        operation.setEditable(false);
        operation.setAllowBlank(true);
        operation.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        operation.setWidth(125);
        operation.setVisible(true);
        operation.getStore().addFilter(new OperFilter());
        operation.getStore().setEnableFilters(true);
        operation.getStore().addAll(operations);
        add(operation, Item.MARGINS);
        add(result = new TextField(), Item.MARGINS);
        result.setReadOnly(true);
        result.getElement().getStyle().setProperty("textAlign", "right");
        fields.addValueChangeHandler(new ValueChangeHandler<CustomHolder>() {
            @Override
            public void onValueChange(ValueChangeEvent<CustomHolder> event) {
                IAggregate op = operation.getValue();
                CustomColumnConfig col = getColumn(event.getValue());
                if (op != null && (col == null || !op.isSupported(col.getClass()))) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            operation.setValue(null);
                        }
                    });
                }
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        operation.getStore().setEnableFilters(false);
                        operation.getStore().setEnableFilters(true);
                    }
                });
            }
        });
    }

    private IAggregateDef getAggregateDef() {
        if (operation.getStore().size() == 0) {
            throw new IllegalStateException("Operation list is empty");
        }
        if (operation.getValue() == null) {
            operation.setValue(operation.getStore().get(0));
        }
        return new AggregateDef(getColumn().getColumn().getAlias(), operation.getValue().getKind());
    }

    public void addQuery(List<IAggregateDef> queries) {
        queries.add(getAggregateDef());
    }
    
    public void readQueryResult(Map<String, ?> res){
        String strVal;
        Object objVal = res.get(getAggregateDef().getAggregateAlias());
        CustomColumnConfig col = getColumn();
        if(objVal == null){
            strVal = "-";
        }else if(col instanceof NumericColumnConfig){
            strVal = new NumberHandler(((NumericColumnConfig)col).getFormat()).formatResultVal((Number)objVal);
        }else if(col instanceof TemporalColumnConfig){
            strVal = new TemporalHandler(((TemporalColumnConfig)col).getFormat()).formatResultVal((Date)objVal);
        }else{
            strVal = objVal.toString();
        }
        result.setText(strVal);
    }

    public void doCalc(Iterable<Row> rows) {
        CustomColumnConfig col = getColumn();
        IAggregate op = operation.getValue();
        String res = null;
        if (col != null && op != null && op.isSupported(col.getClass())) {
            res = op.calc(col, rows);
        }
        result.setValue(res != null ? res : "ERR");
    }

    private CustomColumnConfig getColumn(CustomHolder h) {
        if (h instanceof FieldHolder) {
            return ((FieldHolder) h).getColumn();
        }
        return null;
    }

    private CustomColumnConfig getColumn() {
        return getColumn(fields.getValue());
    }

    private class OperFilter implements Store.StoreFilter<IAggregate> {

        @Override
        public boolean select(Store<IAggregate> store, IAggregate parent, IAggregate item) {
            CustomColumnConfig col = getColumn();
            return col != null && item.isSupported(col.getClass());
        }

    }

}
