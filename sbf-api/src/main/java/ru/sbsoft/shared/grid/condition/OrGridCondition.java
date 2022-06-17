package ru.sbsoft.shared.grid.condition;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public class OrGridCondition implements IGridCondition{
    private List<IGridCondition> cond = new ArrayList<IGridCondition>();

    @Override
    public boolean isMatch(Row row) {
        for(IGridCondition c : cond){
            if(c.isMatch(row)){
                return true;
            }
        }
        return cond.isEmpty();
    }
    
    public void add(IGridCondition cond){
        this.cond.add(cond);
    }
    
}
