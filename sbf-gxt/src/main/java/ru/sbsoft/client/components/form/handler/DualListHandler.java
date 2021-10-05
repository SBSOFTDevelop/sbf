package ru.sbsoft.client.components.form.handler;

import com.google.gwt.cell.client.Cell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.DualListEditField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <M> the model type
 * @param <T> the type displayed in the list view
 * @param <SelfType> self reference
 */
public class DualListHandler<M, F, T, SelfType extends DualListHandler<M, F, T, SelfType>> extends BaseHandler<DualListEditField<M, T>, List<M>, SelfType> {

    private final ValueProvider<? super F, List<M>> allValuesProvider;
    private final ListStore<M> fromStore;
    private final ListStore<M> toStore;
    private final ValueProvider<? super M, T> itemNameProvider;
    private final Cell<T> cell;

    private F model;

    public DualListHandler(String name, String label, ValueProvider<? super F, List<M>> outValueProvider, ModelKeyProvider<? super M> keyProvider, ValueProvider<? super M, T> itemNameProvider, Cell<T> cell) {
        super(name, label);
        this.allValuesProvider = outValueProvider;
        this.fromStore = new ListStore<>(keyProvider);
        this.toStore = new ListStore<>(keyProvider);
        this.itemNameProvider = itemNameProvider;
        this.cell = cell;
    }

    public ListStore<M> getFromStore() {
        return fromStore;
    }

    public ListStore<M> getToStore() {
        return toStore;
    }

    public void setModel(F model) {
        this.model = model;
    }

    private List<M> getAvailableItems() {
        return model == null ? null : allValuesProvider.getValue(model);
    }

    @Override
    protected DualListEditField<M, T> createField() {
        return new DualListEditField<>(fromStore, toStore, itemNameProvider, cell);
    }

    @Override
    public SelfType setVal(List<M> val) {
        fromStore.clear();
        toStore.clear();
        List<M> availableItems = getAvailableItems() != null ? new ArrayList<>(getAvailableItems()) : new ArrayList<>();
        if (val != null) {
            availableItems.removeAll(val);
        }
        fromStore.addAll(availableItems);
        if (val != null) {
            toStore.addAll(val);
        }
        return (SelfType) this;
    }

    @Override
    public List<M> getVal() {
        List<M> vals = toStore.getAll();
        return vals != null && !vals.isEmpty() ? new ArrayList<>(vals) : null;
    }

    @Override
    public SelfType setToolTip(String s) {
        DualListEditField<M, T> f = getField();
        f.getFromView().setToolTip(s + " (" + I18n.get(SBFGeneralStr.labelAvailableValues) + ")");
        f.getToView().setToolTip(s + " (" + I18n.get(SBFGeneralStr.labelSelectedValues) + ")");
        return (SelfType) this;
    }

    @Override
    protected FilterInfo createFilter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ParamInfo createParamInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
