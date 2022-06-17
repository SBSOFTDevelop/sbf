package ru.sbsoft.shared.api.i18n;

/**
 * Класс реализует строку без локлизации с параметрами.
 *
 * @author Sokoloff
 */
public class NonLocalizedParamString extends NonLocalizedString implements IParamString {

    private ILocalizedString[] params;

    public NonLocalizedParamString() {

    }
    
    public NonLocalizedParamString(String value, ILocalizedString... params) {
        super(value);
        this.params = params;
    }

    public NonLocalizedParamString(String value, String... params) {
        this(value, i18nUtils.convertStringParams(params));
    }

    @Override
    public ILocalizedString[] getParams() {
        return params;
    }

}
