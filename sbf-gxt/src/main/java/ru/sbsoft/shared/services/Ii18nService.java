package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import ru.sbsoft.shared.api.i18n.i18nModuleInfo;
import ru.sbsoft.shared.consts.I18nSection;

/**
 * Сервис для получения "динамической" локализации с сервера. Преимущество ее в том (например для
 * GWT), что нет необходимости для каждой локали компилировать отдельное приложение.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@RemoteServiceRelativePath(ServiceConst.I18N_SERVICE_SHORT)
public interface Ii18nService extends SBFRemoteService {

    /**
     * Получить локализацию для указанных модуля и локали.
     *
     * @param locale название локали
     * @param files массив имен файлов ресурсов интернационализации
     * @return информация по локализации
     */
    public i18nModuleInfo geTi18n(String locale, I18nSection[] files);

}
