package ru.sbsoft.dao.operations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.operation.IllegalOperationStatusException;
import ru.sbsoft.shared.model.operation.NoSuchOperationCodeException;
import ru.sbsoft.shared.model.OperationEvent;
import ru.sbsoft.shared.model.operation.OperationException;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationsSelectFilter;

/**
 * Доступ к управлению операцией.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface IMultiOperationDao {

    public Long createOperation(OperationType operationType, String locale, String currentModuleCode, boolean isNeedNotify) throws OperationException, NoSuchOperationCodeException;
    
    
    /**
     * Создает новую операцию на основе кода операции.
     *
     * @param operationType тип создаваемой операции.
     * @param locale имя локали
     * @param currentModuleCode код клиентского модуля
     * @return идентификатор созданной операции.
     * @throws OperationException если не удалось создать операцию.
     * @throws NoSuchOperationCodeException если код операции не найден на сервере.
     */
    public Long createOperation(OperationType operationType, String locale, String currentModuleCode) throws OperationException, NoSuchOperationCodeException;

    /**
     * Создает новую операцию на основе кода операции и имени пользователя.
     *
     * @param operationType тип создаваемой операции.
     * @param locale имя локали
     * @param currentModuleCode код клиентского модуля
     * @param username имя пользователя, создавшего операцию.
     * @return идентификатор созданной операции.
     * @throws OperationException если не удалось создать операцию.
     * @throws NoSuchOperationCodeException если код операции не найден на сервере.
     */
    public Long createOperation(OperationType operationType, String locale, String currentModuleCode, String username) throws OperationException, NoSuchOperationCodeException;

//    /**
//     * Формирует все требующие отображения операции пользователя, отправившего текущий запрос.
//     *
//     * @param filter фильтр, определяющий параметры выборки для отображения операций.
//     * @return список операций пользователя.
//     * @throws OperationException если не удалось сформировать список операций.
//     */
//    public List<OperationInfo> listCurrentUserOperations(OperationsSelectFilter filter) throws OperationException;

    /**
     * Формирует все требующие отображения операции пользователя, отправившего текущий запрос.
     * Работает напрямую через JDBC.
     *
     * @param filter фильтр, определяющий параметры выборки для отображения операций.
     * @return список операций пользователя.
     * @throws SQLException если не удалось сформировать список операций.
     */
    //Вместо listCurrentUserOperations, чтобы не мусорить в log
    public List<OperationInfo> listCurrentUserOperationsJdbc(OperationsSelectFilter filter) throws SQLException;
    
    /**
     * Предоставляет подробную информацию об указанной операции.
     *
     * @param operationId идентификатор операции для получения подробной информации.
     * @return подробная информация об операции.
     * @throws OperationException
     */
    public OperationInfo getOperationInfo(Long operationId) throws OperationException;

    /**
     * Находит код созданной ранее операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @return код созданной ранее операции.
     * @throws OperationException если не удалось найти операцию.
     */
    //public String getOperationCode(Long operationId) throws OperationException;

    /**
     * Устанавливает параметры для ранее созданной операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @param parameters устанавливаемые для операции параметры.
     * @throws OperationException
     */
    public void setOperationParameters(Long operationId, List<OperationObject> parameters) throws OperationException;

    /**
     * Устанавливает параметр с указанным именем для ранее созданной операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @param parameter устанавливаемый для операции параметр.
     * @throws OperationException
     */
    public void setOperationParameter(Long operationId, OperationObject parameter) throws OperationException;

    /**
     * Находит параметры, установленные для указанной операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @return список утановленных для операции параметров.
     * @throws OperationException
     */
    public List<OperationObject> getOperationParameters(Long operationId) throws OperationException;

    /**
     * Находит параметр с указанным именем для указанной операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @param name имя получаемого параметра.
     * @return список утановленных для операции параметров или null, если параметр не найден.
     * @throws OperationException
     */
    public OperationObject getOperationParameter(Long operationId, String name) throws OperationException;

    /**
     * Измнение текущего статуса операции. Старый параметр операции используется для того, чтобы исключить возможность непреднамеренного изменения статуса операции (при одновременной попытке изменения
     * статуса в нескольких потоках).
     *
     * @param operationId идентификатор ранее созданной операции.
     * @param oldStatus предыдущий статус операции.
     * @param newStatus новый статус операции.
     * @throws OperationException
     * @throws IllegalOperationStatusException если текущий статус операции не совпадает с параметром oldStatus.
     */
    public void changeOperationStatus(Long operationId, MultiOperationStatus oldStatus, MultiOperationStatus newStatus) throws OperationException, IllegalOperationStatusException;

   // public void changeOperationStatus(Long operationId, MultiOperationStatus oldStatus, MultiOperationStatus newStatus, boolean isLock) throws OperationException, IllegalOperationStatusException;

    
    
    
    /**
     * Измнение текущего статуса операции в текущей транзакции. Старый параметр операции используется для того, чтобы исключить возможность непреднамеренного изменения статуса операции (при
     * одновременной попытке изменения статуса в нескольких потоках).
     *
     * @param operationId идентификатор ранее созданной операции.
     * @param oldStatus предыдущий статус операции.
     * @param newStatus новый статус операции.
     * @throws OperationException
     * @throws IllegalOperationStatusException если текущий статус операции не совпадает с параметром oldStatus.
     */
    public void changeOperationStatus_SameTransaction(Long operationId, MultiOperationStatus oldStatus, MultiOperationStatus newStatus) throws OperationException, IllegalOperationStatusException;

    /**
     * Находит текущий статус операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @return
     * @throws OperationException
     */
    public MultiOperationStatus getOperationStatus(Long operationId) throws OperationException;

    /**
     * Добавляет в лог операции новую запись.
     *
     * @param opertationId идентификатор ранее созданной операции.
     * @param event содержимое сообщения, сохраняемого в лог.
     * @throws OperationException
     */
    public void writeLog(Long opertationId, OperationEvent event) throws OperationException;

    /**
     * Изменяет вилимость операции для пользователя. Если информация об операции больше не нужна, её можно удалить из списка операций.
     *
     * @param opertationId идентификатор операции.
     * @param visible флаг видимости операции для пользователя.
     * @throws OperationException
     */
    public void setOperationVisible(Long opertationId, boolean visible) throws OperationException;

    /**
     * Возвращает имя пользователя, создавшего укзаанную операцию.
     *
     * @param operationId идентификатор операции.
     * @return имя пользователя, создавшего укзаанную операцию.
     * @throws OperationException
     */
    public String getOperationRunUser(Long operationId) throws OperationException;

    /**
     * Запускает ранее созданную операцию.
     *
     * @param operationId идентификатор ранее созаднной операции, которую необходимо запустить.
     * @throws OperationException
     */
//    public void startOperation(Long operationId) throws OperationException;

    
    public void startOperation(Long operationId) throws OperationException;
    
    
    
    /**
     * Отменяет ранее созданную операцию. Если операция еще не запустилась сервером, то она сразу перейдет в статус "отменена сервером". Если сервер уже запустил операцию на выполнение, то ей сначала
     * присваивается статус "отменена пользователем", после чего обработчик операции получает сигнал о завершении. Если обработчик прервет операцию раньше, чем завершится её выполнение, операция
     * перейдет в статус "отменена сервером", в противном случае операция будет завершена в соответствии с обычным сценарием завершения.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @throws OperationException
     */
    public void cancelOperation(Long operationId) throws OperationException;

    /**
     * Обновляет информацию о текущем прогрессе выполнения операции.
     *
     * @param operationId идентификатор обновляемой операции.
     * @param progress значение в процентах количества выполненных итераций по отношению к общему числу итераций.
     * @param comment отбражаемый комментарий текущего процесса.
     * @throws OperationException
     */
    public void updateOperationProgress(Long operationId, BigDecimal progress, String comment) throws OperationException;

//    /**
//     * Находит все операции с установленным статусом.
//     *
//     * @param status статус, по которому осуществяется выборка.
//     * @return все операции с установленным статусом.
//     * @throws OperationException при ошибке поиска записей.
//     */
//    public List<Long> listAllOpeartionsWithStatus(MultiOperationStatus status) throws OperationException;
//
    /**
     * При постоянном обращении с этим запросом на сервер стал забиваться лог JPA, поэтому решили смотреть напрямую через JDBC.
     *
     * @param status статус, по которому осуществяется выборка.
     * @return все операции с установленным статусом.
     * @throws OperationException
     */
    public List<Long> listAllOpeartionsWithStatusJdbc(MultiOperationStatus status) throws OperationException;
//
//    /**
//     * Возвращает список операций, доступных для запуска, с учетом планировщика.
//     *
//     * @return все операции для запуска.
//     * @throws OperationException
//     */
//    public List<Long> listOperationsToStartJdbc() throws OperationException;

    /**
     * Получает список запусей лога.
     *
     * @param operationId идентификатор операции, для которой считываются записи лога.
     * @param lastLog идентификатор записи лога для начала выборки.
     * @return список записей лога операции.
     * @throws OperationException
     */
    public List<OperationEvent> getOperationLog(Long operationId, Long lastLog) throws OperationException;

    /**
     * Отмечает, уведомлен ли пользователь о результате выполнения операции.
     *
     * @param operationId идентификатор операции, для которой устанавливается флаг уведомления.
     * @param notified флаг уведомления пользователя об окончании выпонения операции.
     * @throws OperationException
     */
    public void setOperationNotified(Long operationId, Boolean notified) throws OperationException;

    /**
     * Отмечает, нужно ли уведомлять пользователя о результате выполнения операции.
     *
     * @param operationId идентификатор операции, для которой устанавливается флаг уведомления.
     * @param notify флаг необходимости уведомления пользователя об окончании выпонения операции.
     * @throws OperationException
     */
    public void setOperationNotify(Long operationId, Boolean notify) throws OperationException;

    public Long createSynchOperation(OperationType operationType, String currentModuleCode, String username) throws OperationException, NoSuchOperationCodeException;
}
