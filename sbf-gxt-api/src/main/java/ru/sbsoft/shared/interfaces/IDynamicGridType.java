package ru.sbsoft.shared.interfaces;

import java.math.BigDecimal;

/**
 *
 * @author sokolov
 */
public interface IDynamicGridType extends NamedGridType {

    String getGroupCode();

    BigDecimal getId();
}
