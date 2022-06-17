package ru.sbsoft.shared.grid.condition;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class AndGridCondition implements IGridCondition{
    private List<IGridCondition> cond = new ArrayList<>();

    @Override
    public boolean isMatch(Row row) {
        for(IGridCondition c : cond){
            if(!c.isMatch(row)){
                return false;
            }
        }
        return true;
    }
    
    public void add(IGridCondition cond){
        this.cond.add(cond);
    }
    
}
