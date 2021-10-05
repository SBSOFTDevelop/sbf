package ru.sbsoft.sbf.app.model;

import java.math.BigDecimal;

/**
 *
 * @author vk
 */
public interface IFormModel {

    BigDecimal getId();

    void setId(BigDecimal id);
    
    String getUiName();

    boolean isReadOnly();
}
