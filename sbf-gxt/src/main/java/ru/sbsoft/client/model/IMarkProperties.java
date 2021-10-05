package ru.sbsoft.client.model;

import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import java.math.BigDecimal;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Обеспечивает доступ к свойствам базовой модели данных для браузера.
 */
public interface IMarkProperties extends PropertyAccess<MarkModel> {

    @Editor.Path("RECORD_ID")
    ModelKeyProvider<MarkModel> key();

    ValueProvider<MarkModel, BigDecimal> RECORD_ID();
}
