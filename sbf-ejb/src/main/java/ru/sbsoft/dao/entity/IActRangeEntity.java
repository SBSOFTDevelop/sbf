package ru.sbsoft.dao.entity;

import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.model.IActRange;

/**
 *
 * @author vk
 */
public interface IActRangeEntity extends IActRange {

    ActRangeFields getActRangeFields();

    void setActRangeFields(ActRangeFields actRangeFields);
    
    @Override
    default YearMonthDay getBegDate(){
        return getActRangeFields() != null ? getActRangeFields().getBegDate() : null;
    }
    
    @Override
    default YearMonthDay getEndDate(){
        return getActRangeFields() != null ? getActRangeFields().getEndDate() : null;
    }
    
    default void setBegDate(YearMonthDay d){
        ActRangeFields f = getActRangeFields();
        if(f == null){
            f = new ActRangeFields();
        }
        f.setBegDate(d);
        setActRangeFields(f);
    }
    
    default void setEndDate(YearMonthDay d){
        ActRangeFields f = getActRangeFields();
        if(f == null){
            f = new ActRangeFields();
        }
        f.setEndDate(d);
        setActRangeFields(f);
    }
}
