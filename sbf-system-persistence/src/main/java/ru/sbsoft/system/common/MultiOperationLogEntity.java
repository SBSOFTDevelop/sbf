package ru.sbsoft.system.common;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Класс сущности "Журнал отложенных операций".
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Entity
@Table(name = "SR_MULTI_OPERATION_LOG")
public class MultiOperationLogEntity extends CommonOperationLogEntity {

    @ManyToOne
    @JoinColumn(referencedColumnName = "RECORD_ID")
    private MultiOperationEntity OPERATION;

    /**
     * Возвращает операцию, которую описывает запись.
     *
     * @return операцию, которую описывает запись.
     */
    public MultiOperationEntity getOPERATION() {
        return OPERATION;
    }

    /**
     * Устанавливает операцию, которую описывает запись.
     *
     * @param OPERATION операция, которую описывает запись.
     */
    public void setOPERATION(MultiOperationEntity OPERATION) {
        this.OPERATION = OPERATION;
    }

}
