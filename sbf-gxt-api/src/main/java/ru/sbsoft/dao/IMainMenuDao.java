package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Интерфейс для посроения главного меню приложения (визуально вертикальная панель, выровненная по левому или правому краю с древовидной иерархией
 * пунктов меню).
 * Для приложений использующих фраймеворк SBF требуется реализация этого интерфейса на сервере (например в nsi в классе {@code MainMenuDaoBean}.
 * <p />В библиотеке реализация отсутствует.
 * @author balandin
 */
@Remote
public interface IMainMenuDao {

    List<MenuItemModel> getChildsMenuItem(BigDecimal folderUQ);
}
