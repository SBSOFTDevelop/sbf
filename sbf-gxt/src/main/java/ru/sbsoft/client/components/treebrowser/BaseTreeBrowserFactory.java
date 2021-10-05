package ru.sbsoft.client.components.treebrowser;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.browser.BrowserButton;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 *
 * @author sokolov
 */
public abstract class BaseTreeBrowserFactory implements IBrowserMaker {

    private final List<TreeBrowserOperation> operations = new ArrayList<>();
    private BrowserButton[] addBt = null;
    protected final NamedGridType gridType;
    private String caption;

    public BaseTreeBrowserFactory(NamedGridType gridType) {
        this.gridType = gridType;
        this.caption = I18n.get(gridType.getItemName());
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
    public ManagedBrowser createBrowser(String idBrowser, String titleBrowser) {
        BaseTreeBrowser browser = createBrowserInstance(this, idBrowser, titleBrowser);
        return browser;
    }

    @Override
    public String getSecurityId() {
        return gridType.getSecurityId();
    }

    public String getCaption() {
        return caption;
    }

    public void initActions(BaseTreeBrowser browser) {
        TreeBrowserConfigHelper h = new TreeBrowserConfigHelper(browser);
        if (isBt(BrowserButton.OPER)) {
            h.setDoubleActionOnToolbar(true);
        }
        browser.setCaption(getCaption());
        if (!operations.isEmpty()) {
            for (TreeBrowserOperation op : operations) {
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
    }

    public BaseTreeBrowserFactory addBt(BrowserButton... addBt) {
        this.addBt = addBt;
        return this;
    }

    public BaseTreeBrowserFactory addReport(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addReport(type, null, useMode);
    }

    public BaseTreeBrowserFactory addReport(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addOperation(type, paramsFormFactory, useMode, OperationKind.REPORT);
    }

    public BaseTreeBrowserFactory addOperation(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addOperation(type, null, useMode);
    }

    public BaseTreeBrowserFactory addOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return addOperation(type, paramsFormFactory, useMode, OperationKind.OPERATION);
    }

    private BaseTreeBrowserFactory addOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, OperationKind kind) {
        operations.add(new TreeBrowserOperation(type, paramsFormFactory, useMode, kind));
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

    protected abstract BaseTreeBrowser createBrowserInstance(final BaseTreeBrowserFactory browserFactory, final String idBrowser, final String titleBrowser);

    private static enum OperationKind {
        OPERATION,
        REPORT
    }

    private static class TreeBrowserOperation {

        private final OperationType type;
        private final IParamFormFactory paramsFormFactory;
        private final TreeTypedGridOperationAction.TYPE_USE_MODE useMode;
        private final OperationKind kind;

        public TreeBrowserOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, OperationKind kind) {
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

        public TreeTypedGridOperationAction.TYPE_USE_MODE getUseMode() {
            return useMode;
        }

        public OperationKind getKind() {
            return kind;
        }

    }
}
