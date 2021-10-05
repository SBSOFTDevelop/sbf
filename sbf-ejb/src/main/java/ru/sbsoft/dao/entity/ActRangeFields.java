package ru.sbsoft.dao.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.model.IActRange;

/**
 *
 * @author Kiselev
 */
@Embeddable
public class ActRangeFields implements IActRange, Serializable {

    @Column(name = "beg_date", nullable = false)
    private YearMonthDay begDate;

    @Column(name = "end_date", nullable = false)
    private YearMonthDay endDate;

    @Override
    public YearMonthDay getBegDate() {
        return begDate;
    }

    public void setBegDate(YearMonthDay begDate) {
        this.begDate = begDate;
    }

    @Override
    public YearMonthDay getEndDate() {
        return endDate;
    }

    public void setEndDate(YearMonthDay endDate) {
        this.endDate = endDate;
    }
}
