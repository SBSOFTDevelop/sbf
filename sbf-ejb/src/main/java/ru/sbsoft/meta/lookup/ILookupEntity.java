package ru.sbsoft.meta.lookup;

import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Интерфейс определяет метод формирования {@link ru.sbsoft.shared.model.LookupInfoModel}.
 *
 * @author Sokoloff
 */
public interface ILookupEntity {

    LookupInfoModel toLookupModel();
}
