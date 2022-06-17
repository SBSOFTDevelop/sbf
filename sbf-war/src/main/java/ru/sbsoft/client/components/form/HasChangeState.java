package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.actions.event.HasChangedStateHandlers;

/**
 *
 * @author vk
 */
public interface HasChangeState extends HasChangedStateHandlers{
    
    boolean isChanged();
    
    void clearChanges();    
}
