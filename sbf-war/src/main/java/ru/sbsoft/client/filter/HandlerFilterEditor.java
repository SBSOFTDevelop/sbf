package ru.sbsoft.client.filter;

import ru.sbsoft.client.components.browser.filter.editor.FilterEditorBuilder;
import ru.sbsoft.shared.FilterEditorConfigBean;
import ru.sbsoft.client.components.form.handler.IFieldHandler;

/**
 *
 * @author Kiselev
 */
public abstract class HandlerFilterEditor implements FilterEditorBuilder {

    @Override
    public final IFieldHandler newInstance(FilterEditorConfigBean config) {
        return create(config.getAlias(), config.getCaption()).setReq(config.isRequired()).setToolTip(config.getDescription());
    }
    
    protected abstract IFieldHandler create(String name, String label);
        
}
