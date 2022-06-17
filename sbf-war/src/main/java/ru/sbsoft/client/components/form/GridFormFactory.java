package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <R>
 */
public abstract class GridFormFactory<M extends IFormModel> extends SimpleFormFactory<M, Row> {

    public GridFormFactory(NamedFormType formType) {
        super(formType);
    }

    protected GridFormFactory(NamedFormContext ctx) {
        super(ctx);
    }
}
