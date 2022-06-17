package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.ui.HasWidgets;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.FormPanelHelper;
import ru.sbsoft.svc.widget.core.client.form.IsField;

/**
 * Групповые операции с полями формы.
 * @author panarin
 */
public class SBFFormPanelHelper extends FormPanelHelper {

    /**
     * Вазывает проверку значения на всех полях.
     * @param container контейнер с полями.
     * @return true, если все проверки успешны.
     */
    public static boolean validate(final HasWidgets container) {
        boolean valid = true;
        for (final IsField<?> field : getFields(container)) {
            if (field instanceof Field) {
                ((Field) field).finishEditing();
            }
            valid &= field.validate(false);
        }
        return valid;
    }

    /**
     * Очищает все поля.
     * @param container контейнер с полями
     */
    public static void clear(final HasWidgets container) {
        for (final IsField<?> field : getFields(container)) {
            field.clear();
        }
    }
}
