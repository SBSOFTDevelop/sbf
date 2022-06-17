package ru.sbsoft.client.components.form.handler;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.core.client.ValueProvider;
import java.util.List;
import ru.sbsoft.client.components.ILookupInfoModelAccessor;
import ru.sbsoft.client.components.list.cell.CustomStyleCell;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author sychugin
 * @param <L>
 * @param <M>
 * @param <SelfType>
 */
public class ExtLookupDualListHandler<L extends LookupInfoModel, M extends IFormModel, SelfType extends ExtLookupDualListHandler<L, M, SelfType>> extends DualListHandler<L, M, String, SelfType> {

    private static final ILookupInfoModelAccessor ACCESSOR = GWT.create(ILookupInfoModelAccessor.class);

    public ExtLookupDualListHandler(String name, String label, ValueProvider<? super M, List<L>> outValueProvider, CustomStyleCell<L> cell) {
        this(name, label, outValueProvider, cell, true);
    }

    public ExtLookupDualListHandler(String name, String label, ValueProvider<? super M, List<L>> outValueProvider, CustomStyleCell<L> cell, boolean withKeyInTitle) {
        super(name, label, outValueProvider, ACCESSOR.key(), withKeyInTitle ? ACCESSOR.semanticKeyAndName() : ACCESSOR.semanticName(), cell);
        if (cell != null) {
            cell.setStores(getFromStore(), getToStore());
        }
    }
}
