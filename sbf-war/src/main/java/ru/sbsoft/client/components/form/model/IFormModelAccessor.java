package ru.sbsoft.client.components.form.model;

import com.google.gwt.editor.client.Editor;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
import java.math.BigDecimal;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface IFormModelAccessor<M extends IFormModel> extends PropertyAccess<M> {

    @Editor.Path("id")
    ModelKeyProvider<IFormModel> key();

    ValueProvider<IFormModel, BigDecimal> id();
}
