package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import java.util.List;

/**
 *
 * @author Kiselev
 */
public interface ISelectionModelAdapter<M> {

    M getSelectedItem();

    List<M> getSelectedItems();

    void setSelection(M item);

    void setSelection(int index);
    
    void selectCell(Grid.GridCell cell);

    Style.SelectionMode getSelectionMode();

    HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<M> handler);
    
    boolean tryInitSelection();
    
    void free();
}
