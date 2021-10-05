package ru.sbsoft.shared.interfaces;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public class LookupTypeFake implements LookupType, DTO {

    private String code;
    private ILocalizedString title;

    private LookupTypeFake() {
    }

    public LookupTypeFake(String code, ILocalizedString title) {
        this.code = code;
        this.title = title;
    }

    @Override
    public ILocalizedString getTitle() {
        return title;
    }

    @Override
    public String getCode() {
        return code;
    }

}
