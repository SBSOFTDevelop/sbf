package ru.sbsoft.client.components.form.model;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
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
