package ru.sbsoft.client.components.operation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.IWindow.WindowHideEvent;
import ru.sbsoft.client.components.IWindow.WindowHideHandler;
import ru.sbsoft.client.components.multiOperation.MultiOperationLogWindow;
import ru.sbsoft.client.components.multiOperation.MultiOperationMonitor;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.*;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.client.schedule.i18n.SBFi18nLocale;
import ru.sbsoft.sbf.app.Registration;

/**
 * Базовый класс операций
 *
 * @author panarin
 */
public abstract class AbstractOperation {

    private Registration paramWindowHideHandler;
    private BaseOperationParamForm paramWindow;
    private OperationCommand command;

    private boolean isShowLogWindow = true;
    private List<CompleteHandler> completeHandlers = new ArrayList<CompleteHandler>();

    public void setShowLogWindow(boolean isShowLogWindow) {
        this.isShowLogWindow = isShowLogWindow;
    }



    public void startOperation() {
        if (paramWindow != null) {
            paramWindow.show();

            paramWindowHideHandler = paramWindow.addHideHandler(new WindowHideHandler() {
                @Override
                public void onHide(final WindowHideEvent event) {
                    final BaseOperationParamForm sourceWindow = (BaseOperationParamForm) event.getSourceWindow();
                    if (sourceWindow.isNeedStart()) {
                        __startOperation(sourceWindow.getParams());
                    }
                }
            });
        } else {
            __startOperation(new ArrayList<ParamInfo>());
        }
    }

    public void startOperation(final List<ParamInfo> params) {
        if (paramWindow != null) {
            throw new IllegalStateException("WTF with paramWindow?!");
        }
        __startOperation(params);
    }

    private void __startOperation(final List<ParamInfo> params) {
        if (paramWindowHideHandler != null) {
            paramWindowHideHandler.remove();
        }

        command = createOperationCommand();
        command.putParams(params);
        command.setModuleName(GWT.getModuleName());
        command.setLocale(SBFi18nLocale.getLocaleName());
        SBFConst.MULTI_OPERATION_SERVICE.startOperation(command, createStartCallback());
    }

    /**
     * Создание специфичной команды операции
     *
     * @return
     */
    protected abstract OperationCommand createOperationCommand();

    private AsyncCallback<Long> createStartCallback() {
        return new DefaultAsyncCallback<Long>() {
            @Override
            public void onResult(final Long operationId) {
                //Слушаем отмену
                MultiOperationMonitor.getInstance().addHandler(new MultiOperationMonitor.SimpleConcreteOperationMonitor(operationId) {

                    @Override
                    public void onNewOperation(OperationInfo operationInfo) {
                        done(operationInfo);
                    }

                    @Override
                    public void onChangeOperation(OperationInfo operationInfo) {
                        done(operationInfo);
                    }

                    private void done(OperationInfo operationInfo) {
                        final MultiOperationStatus status = operationInfo.getStatus();
                        if (status == CANCELED || status == ERROR || status == DONE) {
                            for (final CompleteHandler handler : completeHandlers) {
                                handler.onComplete();
                            }
                            AbstractOperation.this.onCompleted();
                            AbstractOperation.this.onClose(operationInfo.getId());
                        }
                    }
                });
                //показываем лог
                if (isShowLogWindow) {
                    MultiOperationLogWindow.forOperation(operationId, AbstractOperation.this).show();
                }
            }
        };
    }

    public void setParamWindow(final BaseOperationParamForm paramWindow) {
        this.paramWindow = paramWindow;
    }

    public Registration addCompleteHandler(final CompleteHandler handler) {
        completeHandlers.add(handler);
        return new Registration() {
            @Override
            public void remove() {
                completeHandlers.remove(handler);
            }
        };
    }

    public void onCompleted() {
    }

    public void onClose(Long operationId) {
    }

}
