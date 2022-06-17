package ru.sbsoft.shared.renderer;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 * @author balandin
 * @since May 12, 2014 4:38:58 PM
 */
public class BooleanRenderer extends Renderer<Boolean> {

    public BooleanRenderer(I18nResourceInfo trueLabel, I18nResourceInfo falseLabel) {
        addItem(Boolean.FALSE, new LocalizedString(falseLabel));
        addItem(Boolean.TRUE, new LocalizedString(trueLabel));
    }

    public BooleanRenderer(ILocalizedString trueLabel, ILocalizedString falseLabel) {
        addItem(Boolean.FALSE, falseLabel);
        addItem(Boolean.TRUE, trueLabel);
    }

    public BooleanRenderer(String trueLabel, String falseLabel) {
        addItem(Boolean.FALSE, falseLabel);
        addItem(Boolean.TRUE, trueLabel);
    }

    @Override
    public ILocalizedString render(Boolean value) {
        if (value == null) {
            return NonLocalizedString.EMPTY;
        }
        return super.render(value);
    }
}
