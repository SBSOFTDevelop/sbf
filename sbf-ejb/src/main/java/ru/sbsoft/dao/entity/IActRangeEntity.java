package ru.sbsoft.dao.entity;

import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.model.IMutableActRange;

/**
 *
 * @author vk
 */
public interface IActRangeEntity extends IMutableActRange, IBaseEntity {

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
    
    @Override
    default void setBegDate(YearMonthDay d){
        ActRangeFields f = getActRangeFields();
        if(f == null){
            f = new ActRangeFields();
        }
        f.setBegDate(d);
        setActRangeFields(f);
    }
    
    @Override
    default void setEndDate(YearMonthDay d){
        ActRangeFields f = getActRangeFields();
        if(f == null){
            f = new ActRangeFields();
        }
        f.setEndDate(d);
        setActRangeFields(f);
    }
}
