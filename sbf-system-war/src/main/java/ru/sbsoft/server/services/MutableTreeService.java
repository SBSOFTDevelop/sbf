package ru.sbsoft.server.services;

import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IMutableTreeDao;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.services.IMutableTreeService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.MUTABLE_TREE_SERVICE_LONG})
public class MutableTreeService extends SBFRemoteServiceServlet implements IMutableTreeService {

    @EJB
    private IMutableTreeDao treeDao;

    @Override
    public <K extends Number> List<TreeNode<K>> getTreeItems(TreeContext context, TreeNode<K> parent) {
        return treeDao.getTreeItems(context, parent);
    }

    @Override
    public <K extends Number> void setParent(TreeContext context, List<TreeNode<K>> nodes, TreeNode<K> parent) {
        treeDao.setParent(context, nodes, parent);
    }

    @Override
    public <K extends Number> TreeNode<K> getTreeItem(TreeContext context, K key) {
        return treeDao.getTreeItem(context, key);
    }
}
