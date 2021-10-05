package ru.sbsoft.shared.interfaces;

import java.math.BigDecimal;
import ru.sbsoft.shared.NamedFormType;

/**
 *
 * @author sokolov
 */
public interface IDynamicFormType extends NamedFormType {

    String getGroupCode();

    BigDecimal getId();
}
