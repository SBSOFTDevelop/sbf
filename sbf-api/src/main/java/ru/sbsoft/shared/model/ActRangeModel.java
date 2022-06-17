package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author Kiselev
 */
public class ActRangeModel extends TehnologyModel implements IActRangeModel {

    private YearMonthDay begDate;
    private YearMonthDay endDate;

    public ActRangeModel(ActRangeModel m) {
        super(m);
        if(m != null){
            this.begDate = m.begDate;
            this.endDate = m.endDate;
        }
    }
    
    public ActRangeModel() {
    }

    @Override
    public YearMonthDay getBegDate() {
        return begDate;
    }

    @Override
    public void setBegDate(YearMonthDay begDate) {
        this.begDate = begDate;
    }

    @Override
    public YearMonthDay getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }
}
