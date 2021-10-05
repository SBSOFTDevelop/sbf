package ru.sbsoft.client.components.tree;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.dnd.core.client.DND;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.TreeDragSource;
import com.sencha.gxt.dnd.core.client.TreeDropTarget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.IBaseFormOwner;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.sbf.app.form.IHandler;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.TreeNodeModel;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.model.TreeNodeSimpleFormModel;
import ru.sbsoft.shared.services.ITreeService;
import ru.sbsoft.shared.tree.ITreeParentFilterFactory;
import ru.sbsoft.shared.tree.TreeKeyFilterFactory;

/**
 * Универсальная реализация дерева сущностей. Для наполнения данных использует
 * {@link ITreeService}. Также для работы этого дерева проект должен иметь
 * stateless bean реализующий {@link ru.sbsoft.dao.ITeeDao}
 *
 * @see AbstractTree
 */
public class GenericTree<K extends Number> extends AbstractTree<K, TreeNode<K>> implements IBaseFormOwner {
    //private static final Logger log = Logger.getLogger(GenericTree.class.getName());

    private final TreeContext context;
    private final String rootName;
    private final ITreeParentFilterFactory parentFilterFactory = new TreeKeyFilterFactory();
    private final String treeParentIdName = Dict.TREE_PARENT_ID;
    protected ITreeFormFactory<? extends FormModel, K, TreeNode<K>> formFactory;
    private boolean disableEvent = false;

    public GenericTree(final TreeContext context) {
        this(context, null, null);
    }

    public GenericTree(final TreeContext context, ITreeFormFactory<?, K, TreeNode<K>> formFactory) {
        this(context, null, formFactory);
    }

    public GenericTree(final TreeContext context, String rootName) {
        this(context, rootName, null);
    }

    public GenericTree(final TreeContext context, String rootName, ITreeFormFactory<?, K, TreeNode<K>> formFactory) {
        this.context = context;
        this.rootName = rootName;
        this.formFactory = formFactory;
        TreeDragSource<TreeNode<K>> source = new TreeDragSource<TreeNode<K>>(getTree());
        source.addDragStartHandler(new DndDragStartEvent.DndDragStartHandler() {
            @Override
            public void onDragStart(DndDragStartEvent event) {
                TreeNode<K> sel = getTree().getSelectionModel().getSelectedItem();
                if (sel != null && sel == getTree().getStore().getRootItems().get(0)) {
                    event.setCancelled(true);
                    event.getStatusProxy().setStatus(false);
                }
            }
        });
        TreeDropTarget<TreeNode<K>> target = new GenTreeDropTarget();
        target.setAllowSelfAsSource(true);
        target.setFeedback(DND.Feedback.BOTH);
    }

    @Override
    protected void initTools() {
        super.initTools();

        //editToolBar.addSeparator();
        addToolSep();
        addToolBt(ToolButton.SAVE, new EditSelectedTreeAction(SBFBrowserStr.menuOperRowUpdate, SBFResources.BROWSER_ICONS.RowUpdate16(), SBFResources.BROWSER_ICONS.RowUpdate()) {
            @Override
            protected void exec(TreeNode<K> selection) {
                showForm(selection, getParent(selection));
            }
        });

        addToolBt(ToolButton.PLUS, new EditTreeAction(SBFBrowserStr.menuOperRowInsert, SBFResources.BROWSER_ICONS.RowInsert16(), SBFResources.BROWSER_ICONS.RowInsert()) {
            @Override
            protected void exec(TreeNode<K> selection) {
                showForm(null, selection);
            }

            @Override
            public boolean checkEnabled() {
                return super.checkEnabled() && isEditable(null, getSelection()) && checkEnabledAdd(getSelection());
            }
        });

        addToolBt(ToolButton.MINUS, new EditSelectedTreeAction(SBFBrowserStr.menuOperRowDelete, SBFResources.BROWSER_ICONS.RowDelete16(), SBFResources.BROWSER_ICONS.RowDelete()) {
            @Override
            protected void exec(final TreeNode<K> selection) {
                ConfirmMessageBox mb = new ConfirmMessageBox(I18n.get(SBFGeneralStr.captQuery), I18n.get(SBFBrowserStr.msgDeleteSelectedItem));
                mb.addDialogHideHandler(new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            doDeleteNodeIfConfirm(selection);
                        }
                    }
                });
                mb.show();
            }
        });
    }

    public TreeContext getContext() {
        return context;
    }

    public void setTreeFilters(List<FilterInfo> filters) {
        context.setParentFilters(filters);
    }

    public List<FilterInfo> getTreeFilters() {
        return context.getParentFilters();
    }

    public void setFormFactory(ITreeFormFactory formFactory) {
        this.formFactory = formFactory;
        actionManager.updateState();
    }

    protected String getRootName() {
        return rootName;
    }

    protected void createForm(TreeNode<K> node, TreeNode<K> parentNode, AsyncCallback<BaseForm> callback) {
        if (formFactory == null) {
            callback.onSuccess(null);
        } else {
            ((ITreeFormFactory<FormModel, K, TreeNode<K>>) formFactory).createEditForm(node, parentNode, new AsyncCallback<BaseForm<FormModel>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(BaseForm<FormModel> result) {
                    callback.onSuccess(result);
                }
            });
        }
    }

    protected FormContext getFormContext(TreeNode<K> node, TreeNode<K> parentNode) {
        return formFactory != null ? formFactory.getFormContext(node, parentNode) : null;
    }

    protected void doDeleteNodeIfConfirm(final TreeNode<K> selection) {
        SBFConst.FORM_SERVICE.delRecord(getFormContext(selection, getParent(selection)), toFormModelId(selection), new DefaultAsyncCallback<BigDecimal>(GenericTree.this) {
            @Override
            public void onResult(BigDecimal result) {
                getTree().getStore().remove(selection);
            }
        });

    }

    protected boolean checkEnabledAdd(TreeNode<K> parent) {
        return true;
    }

    private TreeNode<K> getParent(TreeNode<K> n) {
        if (disableEvent) {
            return null;
        }

        if (n != null) {
            return getTree().getStore().getParent(n);
        } else {
            throw new IllegalArgumentException("NULL node parent can't be found.");
        }
    }

    private TreeNode<K> getSelection() {
        return getTree().getSelectionModel().getSelectedItem();
    }

    protected boolean isEditable() {
        return formFactory != null;
    }

    protected boolean isEditable(TreeNode<K> node, TreeNode<K> parent) {
        if (!isEditable()) {
            return false;
        }
        FormContext c = formFactory.getFormContext(node, parent);
        return c != null && !RoleCheker.getInstance().isTableReadOnly(c.getFormType().getRights());
    }

    @Override
    protected void loadTreeItems(TreeNode<K> parent, AsyncCallback<List<TreeNode<K>>> callback) {
        SBFConst.MUTABLE_TREE_SERVICE.getTreeItems(getContext(), parent, callback);
    }

    protected void loadTreeItem(K itemKey, AsyncCallback<TreeNode<K>> callback) {
        SBFConst.MUTABLE_TREE_SERVICE.getTreeItem(getContext(), itemKey, callback);
    }

    protected void setParent(List<TreeNode<K>> nodes, TreeNode<K> parent, AsyncCallback<Void> callback) {
        SBFConst.MUTABLE_TREE_SERVICE.setParent(getContext(), nodes, parent, callback);
    }

    private void showForm(final TreeNode<K> node, final TreeNode<K> parent) {
        if (isEditable()) {
            createForm(node, parent, new DefaultAsyncCallback<BaseForm>() {
                @Override
                public void onResult(BaseForm f) {
                    if (f != null) {
                        try {
                            f.setOwner(GenericTree.this);
                            f.addPersistHandler(new IHandler<IFormSaveController.PersistEvent<FormModel>>() {
                                @Override
                                public void onHandle(final IFormSaveController.PersistEvent<FormModel> event) {
                                    handleItemEvent(event, new DefaultAsyncCallback<TreeNode<K>>(GenericTree.this) {
                                        @Override
                                        public void onResult(TreeNode<K> result) {
                                            if (result == null) {
                                                return;
                                            }
                                            TreeNode pn = parent;
                                            if (event.getModel() instanceof TreeNodeSimpleFormModel) {

                                                TreeNodeSimpleFormModel fm = (TreeNodeSimpleFormModel) event.getModel();
                                                pn = (fm.getParentNode() == null) ? null : new TreeNodeModel(fm.getParentNode().getID(), fm.getParentNode().getSemanticName(), false);
                                            }

                                            if (pn != null) {
                                                getTree().getStore().add(pn, result);
                                            } else {
                                                getTree().getStore().add(result);
                                            }

                                        }

                                    });
                                }
                            });
                            f.addEditHandler(new IHandler<IFormSaveController.EditEvent<FormModel>>() {
                                @Override
                                public void onHandle(final IFormSaveController.EditEvent<FormModel> event) {
                                    handleItemEvent(event, new DefaultAsyncCallback<TreeNode<K>>(GenericTree.this) {
                                        @Override
                                        public void onResult(TreeNode<K> result) {

                                            if (event.getModel() instanceof TreeNodeSimpleFormModel) {

                                                TreeNodeSimpleFormModel fm = (TreeNodeSimpleFormModel) event.getModel();
                                                TreeNode pn = (fm.getParentNode() == null) ? null : new TreeNodeModel(fm.getParentNode().getID(), fm.getParentNode().getSemanticName(), false);
                                                TreeNode po = getTree().getStore().getParent(result);
                                                //

                                                toFormModelId(pn);
                                                disableEvent = true;

                                                if (!isEqParent(pn, po)) {
                                                    getTree().getStore().remove(result);

                                                    if (pn != null) {

                                                        getTree().getStore().add(pn, result);
                                                    } else {
                                                        getTree().getStore().add(result);
                                                    }
                                                }

                                            }
                                            disableEvent = false;
                                            getTree().getStore().update(result);

                                        }

                                    });
                                }
                            });
                            f.show(toFormModelId(node));
                        } catch (Throwable ex) {
                            onFailure(ex);
                        }
                    }
                }
            });
        }
    }

    private static boolean isEqParent(TreeNode pn, TreeNode po) {

        if (pn == null && po == null) {
            return true;
        }
        if (pn == null && po != null || pn != null && po == null) {
            return false;
        }
        return pn.getKey().equals(po.getKey());

    }

    private void handleItemEvent(IFormSaveController.ModelEvent<FormModel> event, AsyncCallback<TreeNode<K>> callback) {
        if (event != null && event.getModel() != null) {
            K key = toNodeId(event.getModel());
            if (key != null) {
                loadTreeItem(key, callback);
            }
        }
    }

    @Override
    public boolean isReadOnly(boolean deep) {
        return !isEditable();
    }

    protected BigDecimal toFormModelId(TreeNode<K> node) {
        return formFactory != null ? formFactory.toFormModelId(node) : null;
    }

    protected K toNodeId(FormModel model) {
        return formFactory != null ? formFactory.toNodeId(model) : null;
    }

    @Override
    public void refreshRow(IFormModel model) {
        // save handler is used instead
    }

    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public List<FilterInfo> getParentFilters() {
        if (getTreeFilters().isEmpty()) {
            return Collections.<FilterInfo>singletonList(parentFilterFactory.createFilter(treeParentIdName, getTree().getSelectionModel().getSelectedItem()));
        }
        //добавляем фильтры самого дерева
        List<FilterInfo> list = new ArrayList<>();
        list.add(parentFilterFactory.createFilter(treeParentIdName, getTree().getSelectionModel().getSelectedItem()));
        list.addAll(getTreeFilters());
        return list;
    }

    @Override
    public List<FilterInfo> getDefinedFilters() {
        return Collections.emptyList();
    }

    @Override
    public BigDecimal getClonableRecordID() {
        return null;
    }

    @Override
    public void restoreFocus() {
        getTree().focus();
    }

    @Override
    protected TreeNode<K> createRootNode() {
        return getRootName() != null ? new TreeNodeModel<K>(null, getRootName(), false) : null;
    }

    private abstract class EditTreeAction extends TreeAction {

        private final boolean selectonIndependent;

        public EditTreeAction(I18nResourceInfo caption, ImageResource icon16, ImageResource icon24) {
            this(caption, icon16, icon24, true);
        }

        protected EditTreeAction(I18nResourceInfo caption, ImageResource icon16, ImageResource icon24, boolean selectionIndependent) {
            super(caption, icon16, icon24);
            this.selectonIndependent = selectionIndependent;
        }

        @Override
        protected final void onExecute() {
            final TreeNode<K> sel = getSelection();
            if (selectonIndependent || sel != null) {
                exec(sel);
            }
        }

        protected abstract void exec(TreeNode<K> selection);

        @Override
        public boolean checkEnabled() {
            return super.checkEnabled() && GenericTree.this.isEditable() && (selectonIndependent || getSelection() != null);
        }
    }

    private abstract class EditSelectedTreeAction extends EditTreeAction {

        public EditSelectedTreeAction(I18nResourceInfo caption, ImageResource icon16, ImageResource icon24) {
            super(caption, icon16, icon24, false);
        }

        @Override
        public boolean checkEnabled() {
            TreeNode<K> sel = getSelection();
            return super.checkEnabled() && isEditable(sel, getParent(sel));
        }
    }

    private class GenTreeDropTarget extends TreeDropTarget<TreeNode<K>> {

        public GenTreeDropTarget() {
            super(getTree());
        }

        @Override
        protected void appendModel(final TreeNode<K> p, final List<?> items, final int index) {
            List<TreeNode<K>> moveNodes = new ArrayList<TreeNode<K>>();
            if (items != null) {
                for (Object item : items) {
                    if (item instanceof TreeStore.TreeNode) {
                        TreeNode<K> m = ((TreeStore.TreeNode<TreeNode<K>>) item).getData();
                        if (m != null) {
                            moveNodes.add(m);
                        }
                    } else if (item instanceof TreeNode) {
                        moveNodes.add((TreeNode<K>) item);
                    } else {
                        throw new IllegalArgumentException("Unknown node type: " + item);
                    }
                }
            }
            if (!moveNodes.isEmpty()) {
                setParent(moveNodes, p, new DefaultAsyncCallback<Void>(getTree()) {
                    @Override
                    public void onResult(Void result) {
                        super.onResult(result);
                        GenTreeDropTarget.super.appendModel(p, items, index);
                    }
                });
            }
        }

    }
}
