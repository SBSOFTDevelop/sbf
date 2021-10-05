package ru.sbsoft.client;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.sencha.gxt.state.client.DefaultStateAutoBeanFactory;
import ru.sbsoft.client.components.tree.MenuInfo;

/**
 * Необходим для работы библиотечного (gxt) механизма сохранения состояния меню на клиенте.
 * @author balandin
 * @since Apr 12, 2013 8:18:56 PM
 * @see com.sencha.gxt.state.client.StateManager
 * @see ru.sbsoft.client.components.treemenu.ApplicationContainer
 */
public interface SBFStateAutoBeanFactory extends DefaultStateAutoBeanFactory {

    AutoBean<MenuInfo> menuInfo();
}
