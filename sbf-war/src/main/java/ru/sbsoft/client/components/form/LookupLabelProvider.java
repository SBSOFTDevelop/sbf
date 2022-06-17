package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.common.Strings;

/**
 * Возвращает отображаемое значение по заданному {@link LookupItem}.
 * @author balandin
 * @since Jul 3, 2013 7:40:01 PM
 */
public class LookupLabelProvider implements LabelProvider<LookupItem> {

    public String getLabel(LookupItem item) {
        return item == null ? Strings.EMPTY : String.valueOf(item.getValue());
    }
}
