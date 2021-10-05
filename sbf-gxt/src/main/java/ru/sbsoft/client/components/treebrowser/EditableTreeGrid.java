package ru.sbsoft.client.components.treebrowser;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.TreeStore;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.Initializable;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.IBaseFormOwner;
import ru.sbsoft.client.components.form.IFormFactory;
import ru.sbsoft.client.components.tree.ITreeFormFactory;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.EditableTreeNode;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.filter.BigDecimalFilterInfo;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author sokolov
 * @param <K>
 * @param <N>
 */
public abstract class EditableTreeGrid<K extends BigDecimal, N extends EditableTreeNode<K>> extends AbstractTreeGrid<K, N> implements IBaseFormOwner {

    private final IFormFactory formFactory;
    private boolean cloneable = false;
    private String parentFilterName = Dict.FILTER_PARENT_ID;

    public EditableTreeGrid(NamedGridType gridType, IFormFactory formFactory) {
        super(gridType);
        this.formFactory = formFactory;
        if (formFactory instanceof Initializable) {
            addInitializable((Initializable) formFactory);
        }
    }

    public String getParentFilterName() {
        return parentFilterName;
    }

    public void setParentFilterName(String parentFilterName) {
        this.parentFilterName = parentFilterName;
    }

    public void refreshNode(K itemKey) {
        loadTreeItem(itemKey, new DefaultAsyncCallback<N>() {
            @Override
            public void onResult(N result) {
                TreeStore<N> store = EditableTreeGrid.this.getTreeGrid().getTreeStore();
                N item = store.findModel(result);

                if (item == null) {
                    if (result.getParentKey() == null) {
                        store.add(result);
                    } else {
                        N parent = store.findModelWithKey(result.getParentKey().toString());
                        if (parent != null) {
                            store.add(parent, result);
                            if (parent.isLeaf()) {
                                refreshChildren(parent);
                            }
                        }
                    }

                } else {

                    N parent = store.getParent(result);
                    if ((parent == null && result.getParentKey() == null)
                            || (parent != null && parent.getKey().equals(result.getParentKey()))) {
                        store.update(result);
                    } else {
                        N newParent = null;
                        if (result.getParentKey() != null) {
                            newParent = store.findModelWithKey(result.getParentKey().toString());
                        }
                        store.remove(result);
                        store.add(newParent, result);
                        if (newParent != null) { //надо перечитывать, чтобы изменить лист в ноду
                            refreshChildren(newParent);
                        }
                    }
                }
            }
        });
    }

    private void deleteRow(BigDecimal id) {
        TreeStore<N> store = getTreeGrid().getTreeStore();
        N item = store.findModelWithKey(id.toString());
        if (item != null) {
            store.remove(item);
        }
    }

    protected void tryShowForm(MarkModel model) {
        createEditForm(model, new DefaultAsyncCallback<BaseForm>() {
            @Override
            public void onResult(BaseForm form) {
                if (form != null) {
                    try {
                        form.setOwner(EditableTreeGrid.this);
                        form.show(null == model ? null : model.getRECORD_ID());
                    } catch (Throwable ex) {
                        onFailure(ex);
                    }
                }
            }
        });
    }

    protected void createEditForm(MarkModel model, AsyncCallback<BaseForm> callback) {
        if (formFactory == null) {
            callback.onSuccess(null);
        } else {
            formFactory.createEditForm(model, new AsyncCallback<BaseForm<FormModel>>() {
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

    @Override
    protected MarkModel itemToMarkModel(N item) {
        MarkModel model = new MarkModel();
        model.setRECORD_ID(item.getKey());
        return model;
    }

    @Override
    public void edit() {
        MarkModel selectedModel = getSelectedModel();
        if (selectedModel == null) {
            return;
        }
        tryShowForm(selectedModel);
    }

    @Override
    public void insert() {
        tryShowForm(null);
    }

    @Override
    public void delete() {
        MarkModel selectedModel = getSelectedModel();
        deleteRecord(selectedModel, new DefaultAsyncCallback<BigDecimal>(this) {
            @Override
            public void onResult(BigDecimal result) {
                deleteRow(result);
            }
        });
    }

    @Override
    public boolean isReadOnly(boolean deep) {
        return isReadOnly();
    }

    @Override
    public void refreshRow(IFormModel model) {
        refreshNode((K) model.getId());
    }

    @Override
    public List<FilterInfo> getParentFilters() {
        N selectedModel = getTreeGrid().getSelectionModel().getSelectedItem();
        if (selectedModel == null) {
            return parentFilters;
        }
        List<FilterInfo> list = new ArrayList<>(parentFilters);
        list.add(new BigDecimalFilterInfo(parentFilterName, selectedModel.getKey()));
        return list;
    }

    @Override
    public List<FilterInfo> getDefinedFilters() {
        return null;
    }

    @Override
    public void restoreFocus() {
        getTreeGrid().getView().focus();
    }

    public IFormFactory getFormFactory() {
        return formFactory;
    }

    public void deleteRecord(MarkModel model, DefaultAsyncCallback<BigDecimal> callback) {
        SBFConst.FORM_SERVICE.delRecord(getFormContext(model), model.getRECORD_ID(), callback);
    }

    protected FormContext getFormContext(MarkModel model) {
        return formFactory.getFormContext(model);
    }

    public boolean isCloneable() {
        return cloneable;
    }

    public void setCloneable(boolean cloneable) {
        this.cloneable = cloneable;
    }

    @Override
    public BigDecimal getClonableRecordID() {
        if (cloneable) {
            MarkModel rec = getSelectedModel();
            return rec == null ? null : rec.getRECORD_ID();
        }
        return null;
    }
}
