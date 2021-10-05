package ru.sbsoft.system.common;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;

/**
 * Класс, представляющий сущность "Отложенные операции" - <code>MultiOperationEntity</code>.
 * <p>
 * Поля сущности:</p>
 * <ul>
 * <li><code>OPERATION_CODE</code> - Код операции;</li>
 * <li><code>TITLE</code> - Заголовок операции;</li>
 * <li><code>CREATE_USER</code> - Пользователь, создавший операцию;</li>
 * <li><code>CREATE_DATE</code> - Дата создания операции пользователем;</li>
 * <li><code>RUN_DATE</code> - Когда операция запустилась сначала последний раз (продолжение после паузы не в
 * счет);</li>
 * <li><code>PROGRESS</code> - Прогресс операции от 0% до 100% включительно;</li>
 * <li><code>PROGRESS_COMMENT</code> - Комментарий о ходе операции;</li>
 * <li><code>STATUS</code> - Текущий статус операции;</li>
 * <li><code>CLOSE_USER</code> - Кто отменил операцию;</li>
 * <li><code>CLOSE_DATE</code> - Когда отменил операцию;</li>
 * <li><code>END_DATE</code> - Когда операция завершила выполнение (успешно либо с ошибкой);</li>
 * <li><code>SCHEDULE_DATE</code> - Если дата установлена, то операция будет запущена не раньше неё.;</li>
 * <li><code>CLOSE_COMMENT</code> - Почему отменили операцию;</li>
 * <li><code>CREATE_COMMENT</code> - Если есть комментарии к операции, можно их указать;</li>
 * <li><code>VISIBLE</code> - Видимость операции для пользователя;</li>
 * <li><code>NOTIFIED</code> - Флаг уведомления пользователя о результате выполнения операции;</li>
 * <li><code>NEED_NOTIFY</code> - Флаг необходимости уведомления пользователя о результате выполнения операции;</li>
 * <li><code>APP_CODE</code> - Код приложения для случая, если несколько приложений работают с одной базой;</li>
 * <li><code>MODULE_CODE</code> - Код модуля для случая, если несколько модулей в приложении работают с одной
 * базой;</li>
 * </ul>
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Entity
@Table(name = "SR_MULTI_OPERATION")
@SequenceGenerator(name = MultiOperationEntity.OPERATION_GENERATOR, sequenceName = "SR_SEQUENCE_LOG", allocationSize = 1)
public class MultiOperationEntity extends BaseEntity {

    public static final String OPERATION_GENERATOR = "MultiOperationEntityGenerator";

    @Id
    @GeneratedValue(generator = OPERATION_GENERATOR)
    private Long RECORD_ID;
    private String OPERATION_CODE;
    private String CREATE_USER;
    @Temporal(TemporalType.TIMESTAMP)
    private Date CREATE_DATE;
    @Temporal(TemporalType.TIMESTAMP)
    private Date RUN_DATE;
    private BigDecimal PROGRESS;
    @Enumerated(EnumType.STRING)
    private MultiOperationStatus STATUS;
    @Temporal(TemporalType.TIMESTAMP)
    private Date END_DATE;
    @Temporal(TemporalType.TIMESTAMP)
    private Date SCHEDULE_DATE;
    @Lob
    private String CLOSE_COMMENT;
    @Lob
    private String CREATE_COMMENT;
    private Boolean VISIBLE;
    private Boolean NOTIFIED;
    private Boolean NEED_NOTIFY;
    private String TITLE;
    private String PROGRESS_COMMENT;
    private String APP_CODE;
    private String MODULE_CODE;
    private String LOCALE;

    @OneToMany(mappedBy = "OPERATION")
    public List<MultiOperationLogEntity> logs;

    @OneToMany(mappedBy = "OPERATION")
    @OrderBy("RECORD_ID")
    public List<MultiOperationParameterEntity> parameters;

    @Override
    public Long getIdValue() {
        return getRECORD_ID();
    }

    public Long getRECORD_ID() {
        return RECORD_ID;
    }

    public void setRECORD_ID(Long RECORD_ID) {
        this.RECORD_ID = RECORD_ID;
    }

    public String getOPERATION_CODE() {
        return OPERATION_CODE;
    }

    public void setOPERATION_CODE(String OPERATION_CODE) {
        this.OPERATION_CODE = OPERATION_CODE;
    }

    public String getCREATE_USER() {
        return CREATE_USER;
    }

    public void setCREATE_USER(String CREATE_USER) {
        this.CREATE_USER = CREATE_USER;
    }

    public Date getCREATE_DATE() {
        return CREATE_DATE;
    }

    public void setCREATE_DATE(Date CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public Date getRUN_DATE() {
        return RUN_DATE;
    }

    public void setRUN_DATE(Date RUN_DATE) {
        this.RUN_DATE = RUN_DATE;
    }

    public BigDecimal getPROGRESS() {
        return PROGRESS;
    }

    public void setPROGRESS(BigDecimal PROGRESS) {
        this.PROGRESS = PROGRESS;
    }

    public MultiOperationStatus getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(MultiOperationStatus STATUS) {
        this.STATUS = STATUS;
    }

    public Date getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(Date END_DATE) {
        this.END_DATE = END_DATE;
    }

    public Date getSCHEDULE_DATE() {
        return SCHEDULE_DATE;
    }

    public void setSCHEDULE_DATE(Date SCHEDULE_DATE) {
        this.SCHEDULE_DATE = SCHEDULE_DATE;
    }

    public String getCLOSE_COMMENT() {
        return CLOSE_COMMENT;
    }

    public void setCLOSE_COMMENT(String CLOSE_COMMENT) {
        this.CLOSE_COMMENT = CLOSE_COMMENT;
    }

    public String getCREATE_COMMENT() {
        return CREATE_COMMENT;
    }

    public void setCREATE_COMMENT(String CREATE_COMMENT) {
        this.CREATE_COMMENT = CREATE_COMMENT;
    }

    public Boolean getVISIBLE() {
        return VISIBLE;
    }

    public void setVISIBLE(Boolean VISIBLE) {
        this.VISIBLE = VISIBLE;
    }

    public String getTITLE() {
        return TITLE;
    }

    public Boolean getNOTIFIED() {
        return NOTIFIED;
    }

    public void setNOTIFIED(Boolean NOTIFIED) {
        this.NOTIFIED = NOTIFIED;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public List<MultiOperationLogEntity> getLogs() {
        return logs;
    }

    public List<MultiOperationParameterEntity> getParameters() {
        return parameters;
    }

    public String getPROGRESS_COMMENT() {
        return PROGRESS_COMMENT;
    }

    public void setPROGRESS_COMMENT(String PROGRESS_COMMENT) {
        this.PROGRESS_COMMENT = PROGRESS_COMMENT;
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

    public Boolean getNEED_NOTIFY() {
        return NEED_NOTIFY;
    }

    public void setNEED_NOTIFY(Boolean NEED_NOTIFY) {
        this.NEED_NOTIFY = NEED_NOTIFY;
    }

    public String getLOCALE() {
        return LOCALE;
    }

    public void setLOCALE(String LOCALE) {
        this.LOCALE = LOCALE;
    }

}
