package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.shared.api.i18n.i18nModuleInfo;
import ru.sbsoft.shared.consts.I18nSection;

/**
 * Клиентская часть сервиса интернационализации.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface Ii18nServiceAsync extends ISBFServiceAsync {

    void geTi18n(String locale, I18nSection[] files, AsyncCallback<i18nModuleInfo> moduleInfo);
}
