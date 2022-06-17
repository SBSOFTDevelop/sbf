package ru.sbsoft.client.components.tree;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.widget.core.client.Dialog;
import ru.sbsoft.svc.widget.core.client.selection.SelectionChangedEvent;
import ru.sbsoft.client.components.tree.AbstractTree;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author vk
 */
public class TreeSelectDialog<T extends Number> extends AbstractTreeSelectDialog<T> {

    private HandlerRegistration selectionHReg = null;
    private HandlerRegistration selectLeafOnly = null;

    public TreeSelectDialog(AbstractTree<T, TreeNode<T>> tree) {
        super(tree);
        tree.getTree().getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        super.setPredefinedButtons(Dialog.PredefinedButton.CLOSE);
    }

    public void setSelectLeafOnly(boolean b) {
        if (b && selectLeafOnly == null) {
            selectLeafOnly = getTree().getTree().getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<TreeNode<T>>() {
                @Override
                public void onBeforeSelection(BeforeSelectionEvent<TreeNode<T>> event) {
                    if (!event.getItem().isLeaf()) {
                        event.cancel();
                    }
                }
            });
        } else if (!b && selectLeafOnly != null) {
            selectLeafOnly.removeHandler();
            selectLeafOnly = null;
        }
    }
    
    public void selectVal(final AsyncCallback<TreeNode<T>> callback) {
        selectVal(null, callback);
    }

    public void selectVal(final TreeNode<T> initVal, final AsyncCallback<TreeNode<T>> callback) {
        clearSelectionHandler();
        getTree().getTree().getSelectionModel().deselectAll();
        if (initVal != null) {
            TreeNode<T> sel = findNode(initVal);
            if (sel != null) {
                getTree().getTree().getSelectionModel().select(sel, false);
            }
        }
        selectionHReg = getTree().getTree().getSelectionModel().addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<TreeNode<T>>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<TreeNode<T>> event) {
                if (event.getSelection() != null && !event.getSelection().isEmpty() && event.getSelection().get(0) != null) {
                    callback.onSuccess(event.getSelection().get(0));
                    clearSelectionHandler();
                    TreeSelectDialog.this.hide();
                }
            }
        });
        show();
    }
    
    private void clearSelectionHandler() {
        if (selectionHReg != null) {
            selectionHReg.removeHandler();
            selectionHReg = null;
        }
    }
}
