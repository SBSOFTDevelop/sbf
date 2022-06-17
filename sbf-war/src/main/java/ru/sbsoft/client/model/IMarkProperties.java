package ru.sbsoft.client.model;

import com.google.gwt.editor.client.Editor;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.PropertyAccess;
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
