package ru.sbsoft.client.components.browser.filter.editor;

import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.shared.FilterInfo;

/**
 * Пользовательский редактор
 * @author balandin
 * @param <EDITOR>
 * @since Nov 5, 2015
 */
public interface FilterEditor<EDITOR extends Component> {

    EDITOR getComponent();

    void setFilterInfo(FilterInfo filterInfo);

    boolean isEmpty();

    FilterInfo getFilterInfo();
}
