package ru.sbsoft.client.components.actions.event;

import ru.sbsoft.client.components.ManagedBrowser;

/**
 *
 * @author sokolov
 */
public interface IActivateBrowserEvent {
    
    void onActivate(ManagedBrowser browser);
    
}
