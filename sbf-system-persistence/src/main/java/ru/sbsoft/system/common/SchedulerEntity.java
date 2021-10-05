package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.shared.model.enums.SchedulerStatus;

/**
 * Класс сущности "Планировщик операций".
 * <p>
 * Описание полей класса:
 * <ul>
 * <li><code>CRON_EXPRESSION</code> - Выражение планировщика в сиснтаксисе Cron;</li>
 * <li><code>OPERATION_CODE</code> - Код операции, создаваемой при каждом срабатываении
 * планировщика;</li>
 * <li><code>COUNTER</code> - Количество срабатываний планировщика до его отключения;</li>
 * <li><code>ENABLE_FROM</code> - Метка времени, вплоть до которой планировщик не будет
 * запускаться;</li>
 * <li><code>ENABLE_TO</code> - Метка времени, после которой планировщик перестанет
 * запускаться;</li>
 * <li><code>LAST_RUN</code> - Метка времени последнего запуска планировщика;</li>
 * <li><code>PREV_SCHEDULE</code> - Метка времени предыдущего запуска планировщика;</li>
 * <li><code>NEXT_SCHEDULE</code> - Метка времени очередного запуска планировщика;</li>
 * <li><code>ENABLED</code> - Флаг, определяющий активность планировщика;</li>
 * <li><code>NOTIFY</code> - Флаг, определяющий необходимость оповещения о результатах создаваемых
 * операций;</li>
 * <li><code>STATUS</code> - Текущий статус планировщика;</li>
 * <li><code>IGNORE_BACK</code> - Флаг, определяющий необходимость выполнения каждой итерации
 * планировщика. Если установлен, то будет выполняться лишь итерация, следующая за текущим временем,
 * все пропущенные будут игнорированы;</li>
 * <li><code>USERNAME</code> - Имя пользователя, создавшего планировщик. Операции будут создаваться
 * от имени этого же пользователя;</li>
 * <li><code>APP_CODE</code> - Код приложения для случая, если несколько приложений работают с одной
 * базой;</li>
 * <li><code>MODULE_CODE</code> - Код модуля для случая, если несколько модулей в приложении
 * работают с одной базой;</li>
 * </ul>
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Entity
@Table(name = "SR_SCHEDULER")
@SequenceGenerator(name = SchedulerEntity.OPERATION_GENERATOR, sequenceName = "SR_SEQUENCE_LOG", allocationSize = 1)
public class SchedulerEntity extends BaseEntity implements IFormEntity {

    public static final String OPERATION_GENERATOR = "SchedulerEntityGenerator";

    @Id
    @GeneratedValue(generator = OPERATION_GENERATOR)
    private BigDecimal RECORD_ID;
    private String CRON_EXPRESSION;
    private String OPERATION_CODE;
    private Integer COUNTER;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ENABLE_FROM;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date ENABLE_TO;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date LAST_RUN;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date PREV_SCHEDULE;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date NEXT_SCHEDULE;
    @Enumerated(EnumType.STRING)
    private SchedulerStatus STATUS;
    private boolean ENABLED;
    private boolean NOTIFY;
    private boolean IGNORE_BACK;
    private String USERNAME;
    private String APP_CODE;
    private String MODULE_CODE;

    @OneToMany(cascade = CascadeType.DETACH)
    @JoinTable(
        name = "SR_SCHEDULER_OPERATION",
        joinColumns = @JoinColumn(name = "SCHEDULER_RECORD_ID"),
        inverseJoinColumns = @JoinColumn(name = "OPERATION_RECORD_ID", unique = true)
    )
    public Set<MultiOperationEntity> operations;

    @Override
    public BigDecimal getId() {
        return getRECORD_ID();
    }

    @Override
    public void setId(BigDecimal id) {
        setRECORD_ID(id);
    }

    public BigDecimal getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(BigDecimal RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    @Override
    public Object getIdValue() {
        return getRECORD_ID();
    }

    public String getCRON_EXPRESSION() {
        return CRON_EXPRESSION;
    }

    public void setCRON_EXPRESSION(String CRON_EXPRESSION) {
        this.CRON_EXPRESSION = CRON_EXPRESSION;
    }

    public String getOPERATION_CODE() {
        return OPERATION_CODE;
    }

    public void setOPERATION_CODE(String OPERATION_CODE) {
        this.OPERATION_CODE = OPERATION_CODE;
    }

    public Integer getCOUNTER() {
        return COUNTER;
    }

    public void setCOUNTER(Integer COUNTER) {
        this.COUNTER = COUNTER;
    }

    public Date getENABLE_FROM() {
        return ENABLE_FROM;
    }

    public void setENABLE_FROM(Date ENABLE_FROM) {
        this.ENABLE_FROM = ENABLE_FROM;
    }

    public Date getENABLE_TO() {
        return ENABLE_TO;
    }

    public void setENABLE_TO(Date ENABLE_TO) {
        this.ENABLE_TO = ENABLE_TO;
    }

    public boolean isENABLED() {
        return ENABLED;
    }

    public void setENABLED(boolean ENABLED) {
        this.ENABLED = ENABLED;
    }

    public boolean isNOTIFY() {
        return NOTIFY;
    }

    public void setNOTIFY(boolean NOTIFY) {
        this.NOTIFY = NOTIFY;
    }

    public boolean isIGNORE_BACK() {
        return IGNORE_BACK;
    }

    public void setIGNORE_BACK(boolean IGNORE_BACK) {
        this.IGNORE_BACK = IGNORE_BACK;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public Date getLAST_RUN() {
        return LAST_RUN;
    }

    public void setLAST_RUN(Date LAST_RUN) {
        this.LAST_RUN = LAST_RUN;
    }

    public Date getNEXT_SCHEDULE() {
        return NEXT_SCHEDULE;
    }

    public void setNEXT_SCHEDULE(Date NEXT_SCHEDULE) {
        this.NEXT_SCHEDULE = NEXT_SCHEDULE;
    }

    public String getAPP_CODE() {
        return APP_CODE;
    }

    public void setAPP_CODE(String APP_CODE) {
        this.APP_CODE = APP_CODE;
    }

    public String getMODULE_CODE() {
        return MODULE_CODE;
    }

    public void setMODULE_CODE(String MODULE_CODE) {
        this.MODULE_CODE = MODULE_CODE;
    }

    public SchedulerStatus getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(SchedulerStatus STATUS) {
        this.STATUS = STATUS;
    }

    public Date getPREV_SCHEDULE() {
        return PREV_SCHEDULE;
    }

    public void setPREV_SCHEDULE(Date PREV_SCHEDULE) {
        this.PREV_SCHEDULE = PREV_SCHEDULE;
    }

    public Set<MultiOperationEntity> getOperations() {
        return operations;
    }
}
