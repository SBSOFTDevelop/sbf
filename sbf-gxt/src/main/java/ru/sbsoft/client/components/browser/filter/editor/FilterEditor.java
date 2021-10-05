package ru.sbsoft.client.components.browser.filter.editor;

import com.sencha.gxt.widget.core.client.Component;
import ru.sbsoft.shared.FilterInfo;

/**
 * Пользовательский редактор
 * @author balandin
 * @param <EDITOR>
 * @since Nov 5, 2015
 */
public interface FilterEditor<EDITOR extends Component> {

    public EDITOR getComponent();

    public void setFilterInfo(FilterInfo filterInfo);

    public boolean isEmpty();

    public FilterInfo getFilterInfo();
}
