package ru.sbsoft.sbf.app.model;

import java.math.BigDecimal;
import ru.sbsoft.shared.interfaces.IdItem;

/**
 *
 * @author vk
 */
public interface IFormModel extends IdItem<BigDecimal> {

    @Override
    BigDecimal getId();

    void setId(BigDecimal id);
    
    String getUiName();

    boolean isReadOnly();
}
