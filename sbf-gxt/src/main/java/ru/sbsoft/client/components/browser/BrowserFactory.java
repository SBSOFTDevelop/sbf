package ru.sbsoft.client.components.browser;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.operation.TypedGridOperationAction;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 */
public class BrowserFactory extends BaseBrowserFactory<BrowserFactory> implements IBrowserMaker {

    private final List<Operation> operations = new ArrayList<>();
    private BrowserButton[] addBt = null;

    public BrowserFactory(NamedGridType gridType) {
        super(gridType);
    }

    @Override
    public String getIdBrowser() {
        return gridType.getCode();
    }
    
    @Override
    public String getTitleBrowser() {
        return I18n.get(gridType.getItemName());
    }    

    @Override
    public String getSecurityId() {
        return gridType.getSecurityId();
    }

    public BrowserFactory addBt(BrowserButton... addBt) {
        this.addBt = addBt;
        return this;
    }

    private boolean isBt(BrowserButton b) {
        if (addBt != null) {
            for (BrowserButton bb : addBt) {
                if (bb == b) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public BaseBrowser createBrowser(String idBrowser, String titleBrowser) {
        BaseBrowser b = createBrowserInstance(idBrowser, titleBrowser, createGrid(), getBrowserFlags());
        BrowserConfigHelper h = new BrowserConfigHelper(b);
        if (isBt(BrowserButton.OPER)) {
            h.setDoubleActionOnToolbar(true);
        }
        if (isBt(BrowserButton.MARK)) {
            h.addMarkButtons();
            h.addToolbarSeparator();
        }
        b.setCaption(getCaption());
        if (!operations.isEmpty()) {
            for (Operation op : operations) {
                if (OperationKind.OPERATION == op.getKind()) {
                    if (op.getParamsFormFactory() != null) {
                        h.addOperation(op.getType(), op.getParamsFormFactory(), op.getUseMode());
                    } else {
                        h.addOperation(op.getType(), op.getUseMode());
                    }
                } else {
                    if (op.getParamsFormFactory() != null) {
                        h.addReport(op.getType(), op.getParamsFormFactory(), op.getUseMode());
                    } else {
                        h.addReport(op.getType(), op.getUseMode());
                    }
                }
            }
        }
        return b;
    }

    protected BaseBrowser createBrowserInstance(final String idBrowser, final String titleBrowser, final ContextGrid grid, GridMode... flags) {
        return new BaseBrowser(idBrowser, titleBrowser, createGrid(), flags);
    }

    public BrowserFactory addReport(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addReport(type, null, useMode);
    }

    public BrowserFactory addReport(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addOperation(type, paramsFormFactory, useMode, OperationKind.REPORT);
    }

    public BrowserFactory addOperation(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addOperation(type, null, useMode);
    }

    public BrowserFactory addOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addOperation(type, paramsFormFactory, useMode, OperationKind.OPERATION);
    }

    private BrowserFactory addOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode, OperationKind kind) {
        operations.add(new Operation(type, paramsFormFactory, useMode, kind));
        return this;
    }

    private static enum OperationKind {

        OPERATION, REPORT
    };

    private class Operation {

        private final OperationType type;
        private final IParamFormFactory paramsFormFactory;
        private final TypedGridOperationAction.TYPE_USE_MODE useMode;
        private final OperationKind kind;

        public Operation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode, OperationKind kind) {
            this.type = type;
            this.paramsFormFactory = paramsFormFactory;
            this.useMode = useMode;
            this.kind = kind;
        }

        public OperationType getType() {
            return type;
        }

        public IParamFormFactory getParamsFormFactory() {
            return paramsFormFactory;
        }

        public TypedGridOperationAction.TYPE_USE_MODE getUseMode() {
            return useMode;
        }

        public OperationKind getKind() {
            return kind;
        }

    }

}
