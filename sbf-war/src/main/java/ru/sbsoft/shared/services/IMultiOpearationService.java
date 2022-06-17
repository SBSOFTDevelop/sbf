package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.model.operation.CreateOperationRequest;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationsSelectFilter;
import ru.sbsoft.shared.param.DTO;

/**
 * Сервис для взаимодействия клиента с серверными механизмами управления операциями.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@RemoteServiceRelativePath(ServiceConst.MULTI_OPERATION_SERVICE_SHORT)
public interface IMultiOpearationService extends SBFRemoteService {

    /**
     * Создает новую операцию (без запуска).
     *
     * @param request параметры создаваемой операции.
     * @return
     */
    Long createOperation(CreateOperationRequest request);

    /**
     * Создает новую операцию, устанавливает ей параметры и запускает на исполнение.
     *
     * @param command информация об операции, включая тип и параметры.
     * @return идентификатор созданной операции.
     */
    Long startOperation(OperationCommand command);

    /**
     * Устанавливает параметры для созданной ранее операции.
     *
     * @param operationId идентификатор операции, для которой устанавливаеются параметры.
     * @param parameters параметры, устанавливаемые для указанной операции.
     */
    void setOperationParameters(Long operationId, List<OperationObject> parameters);

    /**
     * Формирует список операций, видимых пользователю.
     *
     * @param filter параметры для фильтрации выборки операций пользователя.
     * @return
     */
    List<OperationInfo> listCurrentUserOperations(OperationsSelectFilter filter);

    /**
     * Возвращает детальную информацию об операции.
     *
     * @param operationId идентификатор операции, данные о которой запрашиваются.
     * @return детальная информация об операции.
     */
    OperationInfo getOperationInfo(Long operationId);

    /**
     * Запускает ранее созданную операцию.
     *
     * @param operationId идентификатор ранее созданной операции.
     */
    void start(Long operationId);

    /**
     * Отменяет выполнение ранее созданной операции.
     *
     * @param operationId идентификатор ранее созданной операции.
     */
    void cancel(Long operationId);

    /**
     * Устанавливает видимость операции в списке пользователя.
     *
     * @param operationId идентификатор ранее созданной операции.
     * @param visible видимость операции в списке пользователя.
     */
    void setVisible(Long operationId, boolean visible);

    /**
     * Получает значение параметра с указанным именем для операции.
     *
     * @param operationId идентификатор операции.
     * @param parameterName название параметра, значение которого требуется получить.
     * @return значение параметра операции.
     */
    DTO getOperationParameter(Long operationId, String parameterName);

    /**
     * Отмечает, уведомлен ли пользователь о результате выполнения операции.
     *
     * @param operationId идентификатор операции.
     * @param notified флаг уведомления пользователя об окончании выпонения операции.
     */
    void setUserNotified(Long operationId, Boolean notified);
}
