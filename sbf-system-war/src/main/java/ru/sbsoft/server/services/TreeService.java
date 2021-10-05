package ru.sbsoft.server.services;

import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.ITreeDao;
import ru.sbsoft.shared.TreeContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.services.ITreeService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.TREE_SERVICE_LONG})
public class TreeService extends SBFRemoteServiceServlet implements ITreeService {

    @EJB
    private ITreeDao treeDao;

    @Override
    public <K extends Number> List<TreeNode<K>> getTreeItems(TreeContext context, TreeNode<K> parent) {
        return treeDao.getTreeItems(context, parent);
    }
}
