package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.param.DTO;

/**
 * Интерфейс представления локализованных строковых параметов.
 * @author Sokoloff
 */
public interface IParamString extends DTO {

    ILocalizedString[] getParams();
}
