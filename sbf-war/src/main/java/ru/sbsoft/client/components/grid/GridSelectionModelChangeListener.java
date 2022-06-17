package ru.sbsoft.client.components.grid;

import ru.sbsoft.svc.widget.core.client.grid.GridSelectionModel;

/**
 *
 * @author Kiselev
 */
public interface GridSelectionModelChangeListener<M> {

    void onGridSelectionModelChanged(GridSelectionModel<M> newModel);
}
