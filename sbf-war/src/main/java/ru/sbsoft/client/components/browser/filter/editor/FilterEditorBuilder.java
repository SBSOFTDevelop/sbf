package ru.sbsoft.client.components.browser.filter.editor;

import ru.sbsoft.shared.FilterEditorConfigBean;

/**
 * @author balandin
 * @since Nov 5, 2015
 */
public interface FilterEditorBuilder {

    FilterEditor newInstance(FilterEditorConfigBean config);

}
