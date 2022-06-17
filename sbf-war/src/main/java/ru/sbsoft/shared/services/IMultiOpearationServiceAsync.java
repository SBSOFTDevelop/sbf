package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.meta.BigDecimalListWrapper;
import ru.sbsoft.shared.model.operation.CreateOperationRequest;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationsSelectFilter;

/**
 * GWT-представление для сервиса управления операциями.
 *
 * @see IMultiOpearationService
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface IMultiOpearationServiceAsync extends ISBFServiceAsync {

    /**
     * @see IMultiOpearationService#createOperation
     */
    void createOperation(CreateOperationRequest request, AsyncCallback<Long> callback);

    /**
     * @see IMultiOpearationService#startOperation
     */
    void startOperation(OperationCommand command, AsyncCallback<Long> createStartCallback);

    /**
     * @see IMultiOpearationService#setOperationParameters
     */
    void setOperationParameters(Long operationId, List<OperationObject> parameters, AsyncCallback<Void> callback);

    /**
     * @see IMultiOpearationService#listCurrentUserOperations
     */
    void listCurrentUserOperations(OperationsSelectFilter filter, AsyncCallback<List<OperationInfo>> callback);

    /**
     * @see IMultiOpearationService#getOperationInfo
     */
    void getOperationInfo(Long operationId, AsyncCallback<OperationInfo> callback);

    /**
     * @see IMultiOpearationService#start
     */
    void start(Long operationId, AsyncCallback<Void> callback);

    /**
     * @see IMultiOpearationService#cancel
     */
    void cancel(Long operationId, AsyncCallback<Void> callback);

    /**
     * @see IMultiOpearationService#setVisible
     */
    void setVisible(Long operationId, boolean visible, AsyncCallback<Void> callback);

    /**
     * @see IMultiOpearationService#getOperationParameter
     */
    void getOperationParameter(Long operationId, String parameterName, AsyncCallback<BigDecimalListWrapper> callback);

    /**
     * @see IMultiOpearationService#setUserNoticed
     */
    void setUserNotified(Long operationId, Boolean notified, AsyncCallback<Void> callback);

}
