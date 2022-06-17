package ru.sbsoft.client.components.tree;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.cell.core.client.form.SvcYearMonthDayConverter;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.widget.core.client.Dialog;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.Date;
import ru.sbsoft.client.components.tree.AbstractTree;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.model.IActRange;

/**
 *
 * @author vk
 */
public abstract class AbstractTreeSelectDialog<T extends Number> extends Dialog {

    private final AbstractTree<T, TreeNode<T>> tree;

    public AbstractTreeSelectDialog(AbstractTree<T, TreeNode<T>> tree) {
        this.tree = tree;
        tree.getTree().setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String k = (String) context.getKey();
                TreeStore<TreeNode<T>> ts = tree.getTree().getStore();
                TreeNode<T> n = ts.findModelWithKey(k);
                if (n.isLeaf() && n instanceof IActRange) {
                    YearMonthDay db = ((IActRange) n).getBegDate();
                    YearMonthDay de = ((IActRange) n).getEndDate();
                    String hint = "c " + db + (de != null ? " по " + de : "");
                    sb.appendHtmlConstant("<span title=\"" + hint + "\"");
                    if (de != null && de.compareTo(SvcYearMonthDayConverter.convert(new Date())) < 0) {
                        sb.appendHtmlConstant(" style=\"color: gray\"");
                    }
                    sb.appendHtmlConstant(">" + value + "</span>");
                } else {
                    sb.appendHtmlConstant(value);
                }
            }
        });
        super.setModal(true);
        super.setBodyStyleName("pad-text");
        super.getBody().addClassName("pad-text");
        super.setHideOnButtonClick(true);
        super.setPixelSize(600, 600);
        super.setWidget(tree);
    }

    public TreeNode<T> findNode(TreeNode<T> node) {
        if (node != null) {
            Tree<TreeNode<T>, String> gtree = tree.getTree();
            String strKey = getStrKey(node);
            TreeNode<T> sel = gtree.getSelectionModel().getSelectedItem();
            if (sel != null && getStrKey(sel).equals(strKey)) {
                return sel;
            }
            return gtree.getStore().findModelWithKey(strKey);
        }
        return null;
    }

    public String getStrKey(TreeNode<T> node) {
        return tree.getNodeProps().key().getKey(node);
    }

    protected AbstractTree<T, TreeNode<T>> getTree() {
        return tree;
    }
}
