package ru.sbsoft.server.services;

import ru.sbsoft.server.services.SBFRemoteServiceServlet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IApplicationDao;
import ru.sbsoft.dao.IMainMenuDao;
import ru.sbsoft.model.ApplicationMenuModel;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.shared.services.IDBStructService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.DB_STRUCT_SERVICE_LONG})
public class DBStructService extends SBFRemoteServiceServlet implements IDBStructService {

    @EJB
    private IApplicationDao applicationDao;
    @EJB
    private IMainMenuDao mainMenuDao;

    @Override
    public List<MenuItemModel> getChildsMenuItem(final MenuItemModel folder) {
        if (folder == null) {
            return Collections.EMPTY_LIST;
        }
        return mainMenuDao.getChildsMenuItem(folder.getID());
    }

    @Override
    public List<ApplicationMenuTreeModel> getApplicationList(final List<String> appNames) {
        final List<ApplicationMenuModel> loadedList = applicationDao.getApplicationList(appNames);
        final List<ApplicationMenuTreeModel> resultList = new ArrayList<>(loadedList.size());
        for (final ApplicationMenuModel loadedModel : loadedList) {
            final ApplicationMenuTreeModel r = new ApplicationMenuTreeModel();
            r.setAPPLICATION_CODE(loadedModel.getAPPLICATION_CODE());
            r.setAPPLICATION_TITLE(loadedModel.getAPPLICATION_TITLE());
            r.setRootMenuList(loadedModel.getRootMenuList());
            resultList.add(r);
        }
        return resultList;
    }
}
