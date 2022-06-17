package ru.sbsoft.shared.grid.condition;

import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author vk
 */
public class GridEditVal<T> implements DTO {

    private IColumn col;
    private Wrapper<T> val;

    public GridEditVal(IColumn col, Wrapper<T> val) {
        this.col = col;
        this.val = val;
    }

    private GridEditVal() {
    }

    public IColumn getCol() {
        return col;
    }

    public Wrapper<T> getVal() {
        return val;
    }
}
