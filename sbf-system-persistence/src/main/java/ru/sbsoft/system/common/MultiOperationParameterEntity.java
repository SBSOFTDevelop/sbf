package ru.sbsoft.system.common;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import ru.sbsoft.dao.entity.BaseEntity;

/**
 * Класс сущности "Параметры отложенных операций".
 * <p>Содержит поля:</p>
 * <ul>
 * <li><code>OPERATION_RECORD_ID</code>  - Код операции;</li>
 * <li><code>PARAM_NAME</code> -Название параметра;</li>
 * <li><code>PARAM_CLASS</code>-Класс значения параметра;</li>
 * <li><code>PARAM_VALUE</code>- Значение параметра;</li>
 * </ul>
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Entity
@Table(name = "SR_MULTI_OPERATION_PARAM")
@SequenceGenerator(name = MultiOperationParameterEntity.OPERATION_GENERATOR, sequenceName = "SR_SEQUENCE_LOG", allocationSize = 1)
public class MultiOperationParameterEntity extends BaseEntity {

    public static final String OPERATION_GENERATOR = "MultiOperationParameterEntityGenerator";

    @Id
    @GeneratedValue(generator = OPERATION_GENERATOR)
    private Long RECORD_ID;

    @ManyToOne
    @JoinColumn(referencedColumnName = "RECORD_ID")
    private MultiOperationEntity OPERATION;

    private String PARAM_NAME;
    private String PARAM_CLASS;
    @Lob
    private String PARAM_VALUE;

    public Long getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(Long RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    /**
     * Возвращает параметризуемую операцию.
     *
     * @return параметризуемую операцию.
     */
    public MultiOperationEntity getOperation() {
        return OPERATION;
    }

    /**
     * Устанавливает параметризуемую операцию.
     *
     * @param operation параметризуемая операция.
     */
    public void setOperation(MultiOperationEntity operation) {
        this.OPERATION = operation;
    }

    /**
     * Возвращает название параметра.
     *
     * @return название параметра.
     */
    public String getPARAM_NAME() {
        return PARAM_NAME;
    }

    /**
     * Устанавливает название параметра.
     *
     * @param PARAM_NAME название параметра.
     */
    public void setPARAM_NAME(String PARAM_NAME) {
        this.PARAM_NAME = PARAM_NAME;
    }

    /**
     * Возвращает название класса, описывающего значение параметра.
     *
     * @return название класса, описывающего значение параметра.
     */
    public String getPARAM_CLASS() {
        return PARAM_CLASS;
    }

    /**
     * Устанавливает название класса, описывающего значение параметра.
     *
     * @param PARAM_CLASS название класса, описывающего значение параметра.
     */
    public void setPARAM_CLASS(String PARAM_CLASS) {
        this.PARAM_CLASS = PARAM_CLASS;
    }

    /**
     * Возвращает значение параметра в формате JSON.
     *
     * @return значение параметра в формате JSON.
     */
    public String getPARAM_VALUE() {
        return PARAM_VALUE;
    }

    /**
     * Устанавливает значение параметра в формате JSON.
     *
     * @param PARAM_VALUE значение параметра в формате JSON.
     */
    public void setPARAM_VALUE(String PARAM_VALUE) {
        this.PARAM_VALUE = PARAM_VALUE;
    }

    @Override
    public Long getIdValue() {
        return getRECORD_ID();
    }

}
