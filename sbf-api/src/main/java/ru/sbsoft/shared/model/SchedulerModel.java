package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.FormModel;
import java.util.Date;
import ru.sbsoft.shared.model.enums.SchedulerStatus;

/**
 * Класс модели "Планировщик операций".
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
 * <li><code>NOTIFY</code> - Флаг, определяющий необходимость оповещения о результатах создаваемых операций;</li>
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
public class SchedulerModel extends FormModel {

    private String CRON_EXPRESSION;
    private String OPERATION_CODE;
    private Integer COUNTER;
    private Date ENABLE_FROM;
    private Date ENABLE_TO;
    private Date LAST_RUN;
    private Date PREV_SCHEDULE;
    private Date NEXT_SCHEDULE;
    private SchedulerStatus STATUS;
    private boolean ENABLED;
    private boolean NOTIFY;
    private boolean IGNORE_BACK;
    private String LAST_ERROR;
    private String MODULE_CODE;

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

    public Date getLAST_RUN() {
        return LAST_RUN;
    }

    public void setLAST_RUN(Date LAST_RUN) {
        this.LAST_RUN = LAST_RUN;
    }

    public Date getPREV_SCHEDULE() {
        return PREV_SCHEDULE;
    }

    public void setPREV_SCHEDULE(Date PREV_SCHEDULE) {
        this.PREV_SCHEDULE = PREV_SCHEDULE;
    }

    public Date getNEXT_SCHEDULE() {
        return NEXT_SCHEDULE;
    }

    public void setNEXT_SCHEDULE(Date NEXT_SCHEDULE) {
        this.NEXT_SCHEDULE = NEXT_SCHEDULE;
    }

    public SchedulerStatus getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(SchedulerStatus STATUS) {
        this.STATUS = STATUS;
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

    public String getLAST_ERROR() {
        return LAST_ERROR;
    }

    public void setLAST_ERROR(String LAST_ERROR) {
        this.LAST_ERROR = LAST_ERROR;
    }

    public String getMODULE_CODE() {
        return MODULE_CODE;
    }

    public void setMODULE_CODE(String MODULE_CODE) {
        this.MODULE_CODE = MODULE_CODE;
    }

}
