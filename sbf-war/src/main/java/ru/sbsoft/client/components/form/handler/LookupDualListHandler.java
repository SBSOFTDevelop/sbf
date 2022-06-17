package ru.sbsoft.client.components.form.handler;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.core.client.ValueProvider;
import java.util.List;
import ru.sbsoft.client.components.ILookupInfoModelAccessor;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class LookupDualListHandler<SelfType extends LookupDualListHandler<SelfType>> extends DualListHandler<LookupInfoModel, FormModel, String, SelfType> {

    private static final ILookupInfoModelAccessor ACCESSOR = GWT.create(ILookupInfoModelAccessor.class);

    public LookupDualListHandler(String name, String label, ValueProvider<? super FormModel, List<LookupInfoModel>> valueProvider) {
        super(name, label, valueProvider, ACCESSOR.key(), ACCESSOR.semanticKeyAndName(), new TextCell());

    }
}
