package ru.sbsoft.client.components.form.handler;

import com.google.gwt.i18n.client.NumberFormat;
import ru.sbsoft.svc.widget.core.client.form.BigDecimalField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class CurrencyHandler<SelfType extends CurrencyHandler<SelfType>> extends BigDecimalHandler<SelfType> {

    public CurrencyHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public CurrencyHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected BigDecimalField createField() {
        BigDecimalField f = super.createField();
        f.setFormat(NumberFormat.getFormat("#,##0.00 \u20BD"));
        return f;
    }
}
