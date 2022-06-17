package ru.sbsoft.dao;

import java.util.List;
import javax.ejb.Remote;
import ru.sbsoft.model.ApplicationMenuModel;
import ru.sbsoft.model.IOperationSettings;

/**
 * Настройки приложения. 
 * Подразумевают реализацию в виде EJB в каждом приложении.
 */
@Remote
public interface IApplicationDao {

    /**
     * Возвращает список разделов (корневых пунктов меню).
     * @param apps Список кодов разделов, которые требуется отобразить. Если null - выдаются все даступные пользователю разделы приложения.
     * @return Список разделов
     */
    List<ApplicationMenuModel> getApplicationList(List<String> apps);

    /**
     * Уникальный код приложения.
     * Был введен для использования в работе механизма запуска операций
     * @return Код приложения
     */
    String getAppCode();

    /**
     * Настройки операций, зависящие от операций.
     * Будут расширяться по необходимости
     * @return Объект настроек
     */
    IOperationSettings getOperationSettings();
    
}
