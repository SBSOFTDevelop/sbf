package ru.sbsoft.client.components.operation;

import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.appmenu.MenuItemModelEx;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Управляет операциями в приложении. 
 * Регистрирует их и перенаправляет запросы на их запуск.
 * Базовый абстрактный класс для всех менеджеров операций приложений, использующих фрамеворк SBF.
 * <p />Приложение на основе фрамеворка SBF должно содержать класс расширяющий {@code BaseOperationManager}.
 * <p> Обычно класс-наследник создается как singleton object (один экземпляр длоя одного svc приложения) и перепопределяется метод {@link #registrOperations},
 * в котором добавляются операции, используемые в этом приложении.
 * <p>Например:
 * <pre>
 * public class OperationManager extends BaseOperationManager {
 *
 *   private final static OperationManager INSTANCE = new OperationManager();
 *
 *   public static OperationManager getInstance() {
 *       return INSTANCE;
 *   }
 *
 *   @Override
 *   protected void registrOperations() throws BrowserException {
 *        addInstance(new KLADRImportOperationMaker());
 *        ....
 *   }
 * }
 * </pre>
 * GWT приложение на основе фрамеворка SBF расширяет класс {@link ru.sbsoft.client.SBFEntryPoint}.
 * В конструкторе класса-наследника необходимо проинициализировать поле {@code protected BaseOperationManager operationManager}
 *     экземпляром {@code BaseOperationManager}.
 * <p> Например:
 * <pre> 
 * public class AppEntryPoint extends SBFEntryPoint {
 *
 *   public AppEntryPoint() {
 *       super();
 *       ...
 *       //
 *       this.browserManager = BrowserManager.getInstance();
 *       ...
 *   }
 * </pre>
 * <p/>
 */
public abstract class BaseOperationManager {

    private final Map<String, IOperationMaker> registrOperations = new HashMap<String, IOperationMaker>();

    public void initManager() throws BrowserException {
        registrOperations();
    }

    /**
     * Первоначальная регистрация создателей операций
     *
     * <p /> Например:
     *
     * <pre>
     * protected void registrOperations() throws BrowserException {
     * addInstance(new ExportOperationMaker());
     * }
     * </pre>
     *
     * @throws BrowserException
     */
    protected abstract void registrOperations() throws BrowserException;

    private AbstractOperation getRegisteredOperation(final String operationId) throws BrowserException {
        final IOperationMaker maker = registrOperations.get(getOperationCode(operationId));
        if (null == maker) {
            throw new BrowserException(I18n.get(SBFExceptionStr.operNotRegistered),
                    new Object[]{operationId});
        }
        return maker.createOperation();
    }

    protected String getOperationCode(final String operationId) {
        return operationId;
    }

    public void openOperation(final MenuItemModel model) throws BrowserException {
        switch (model.getMenuType()) {
            case Operation:
            case OperationSingle:
            case Report:
                break;
            default:
                throw new BrowserException(I18n.get(SBFExceptionStr.commandInvalidType, model.getMenuName()));
        }
        if (model instanceof MenuItemModelEx) {
            ((MenuItemModelEx)model).getOperationMaker().createOperation().startOperation();
        } else {
            openOperation(model.getOperationID());
        }
    }

    public void openOperation(final String operationID) throws BrowserException {
        final AbstractOperation operation = getRegisteredOperation(operationID);
        operation.startOperation();
    }

    protected void addInstance(final IOperationMaker maker) throws BrowserException {
        String code = maker.getOperationCode();
        if (registrOperations.containsKey(code)) {
            throw new BrowserException(I18n.get(SBFExceptionStr.operAlreadyRegistr, maker.getOperationCode()));
        }
        registrOperations.put(code, maker);
    }
}
