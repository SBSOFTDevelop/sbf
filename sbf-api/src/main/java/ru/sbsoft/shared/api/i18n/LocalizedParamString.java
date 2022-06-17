package ru.sbsoft.shared.api.i18n;

/**
 * Класс реализует локазизацию строки с параметрами.
 *
 * @author Sokoloff
 */
public class LocalizedParamString extends LocalizedString implements IParamString {

    private ILocalizedString[] params;

    public LocalizedParamString() {
    }

    public LocalizedParamString(I18nResourceInfo resourceInfo, ILocalizedString... params) {
        super(resourceInfo);
        this.params = params;
    }

    public LocalizedParamString(I18nResourceInfo resourceInfo, String... params) {
        super(resourceInfo);
        this.params = i18nUtils.convertStringParams(params);
    }

    @Override
    public ILocalizedString[] getParams() {
        return params;
    }

}
