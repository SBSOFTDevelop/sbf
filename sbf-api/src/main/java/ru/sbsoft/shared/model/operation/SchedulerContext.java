package ru.sbsoft.shared.model.operation;

import java.util.Date;
import ru.sbsoft.shared.param.DTO;

/**
 * Информация о планировщике операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SchedulerContext implements DTO {

    private Long schedulerId;
    private Date startDate;
    private Date nextSchedule;
    private Date prevSchedule;
    private Date lastRun;

    /**
     * @return идентификатор планировщика.
     */
    public Long getSchedulerId() {
        return schedulerId;
    }

    /**
     * @param schedulerId идентификатор планировщика.
     */
    public void setSchedulerId(Long schedulerId) {
        this.schedulerId = schedulerId;
    }

    /**
     * @return время активации планировщика.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate время активации планировщика
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return дата очередной итерации.
     */
    public Date getNextSchedule() {
        return nextSchedule;
    }

    /**
     * @param nextSchedule дата очередной итерации.
     */
    public void setNextSchedule(Date nextSchedule) {
        this.nextSchedule = nextSchedule;
    }

    /**
     * @return дата предыдущей итерации.
     */
    public Date getPrevSchedule() {
        return prevSchedule;
    }

    /**
     * @param prevSchedule дата предыдущей итерации.
     */
    public void setPrevSchedule(Date prevSchedule) {
        this.prevSchedule = prevSchedule;
    }

    /**
     * @return дата запуска.
     */
    public Date getLastRun() {
        return lastRun;
    }

    /**
     * @param lastRun дата запуска.
     */
    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }

    @Override
    public String toString() {
        return "SchedulerContext{" + "schedulerId=" + schedulerId + ", startDate=" + startDate + ", nextSchedule=" + nextSchedule + ", prevSchedule=" + prevSchedule + ", lastRun=" + lastRun + '}';
    }

}
