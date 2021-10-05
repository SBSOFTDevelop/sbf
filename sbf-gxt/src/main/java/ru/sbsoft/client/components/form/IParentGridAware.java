package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface IParentGridAware<M extends MarkModel> {

    void setParentGrid(BaseGrid<M> g);
}
