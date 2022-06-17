package ru.sbsoft.client.components.tree;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.widget.core.client.Dialog;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.tree.AbstractTree;
import ru.sbsoft.shared.TreeNode;

/**
 *
 * @author vk
 */
public class TreeMultiSelectDialog<T extends Number> extends AbstractTreeSelectDialog<T> {

    private HandlerRegistration selectionHReg = null;
    private final TextButton applyBt = new TextButton("Применить");
    
    public TreeMultiSelectDialog(AbstractTree<T, TreeNode<T>> tree) {
        super(tree);
        tree.getTree().getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        // Disable selection
        tree.getTree().getSelectionModel().addBeforeSelectionHandler(ev -> ev.cancel());
        tree.getTree().setCheckable(true);
        tree.getTree().setCheckStyle(Tree.CheckCascade.NONE);
        super.setPredefinedButtons(Dialog.PredefinedButton.CANCEL);
        super.addButton(applyBt);
    }

    public void setSelectLeafOnly(boolean b) {
        if (b) {
            getTree().getTree().setCheckNodes(Tree.CheckNodes.LEAF);
        } else {
            getTree().getTree().setCheckNodes(Tree.CheckNodes.BOTH);
        }
    }

    public void selectVal(final AsyncCallback<List<TreeNode<T>>> callback) {
        selectVal(null, callback);
    }

    public void selectVal(final TreeNode<T> initVal, final AsyncCallback<List<TreeNode<T>>> callback) {
        clearSelectionHandler();
        final Tree<TreeNode<T>, String> tree = getTree().getTree();
        tree.getSelectionModel().deselectAll();
        if (initVal != null) {
            List<TreeNode<T>> initSel = new ArrayList<>();
            initSel.add(initVal);
            tree.setCheckedSelection(initSel);
        }else{
            tree.setCheckedSelection(new ArrayList<>());
        }
        selectionHReg = applyBt.addSelectHandler(ev -> {
            clearSelectionHandler();
            List<TreeNode<T>> sel = new ArrayList(tree.getCheckedSelection());
            callback.onSuccess(sel);
            TreeMultiSelectDialog.this.hide();
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
