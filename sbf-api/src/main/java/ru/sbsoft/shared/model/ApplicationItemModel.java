package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author Sokoloff
 */
public class ApplicationItemModel implements Serializable {

    private BigDecimal ID;
    private String APPLICATION_CODE;
    private String APPLICATION_TITLE;
    private BigDecimal MENU_ID;

    public ApplicationItemModel() {
    }

    public BigDecimal getID() {
        return ID;
    }

    public void setID(BigDecimal ID) {
        this.ID = ID;
    }

    public String getAPPLICATION_CODE() {
        return APPLICATION_CODE;
    }

    public void setAPPLICATION_CODE(String APPLICATION_CODE) {
        this.APPLICATION_CODE = APPLICATION_CODE;
    }

    public String getAPPLICATION_TITLE() {
        return APPLICATION_TITLE;
    }

    public void setAPPLICATION_TITLE(String APPLICATION_TITLE) {
        this.APPLICATION_TITLE = APPLICATION_TITLE;
    }

    public BigDecimal getMENU_ID() {
        return MENU_ID;
    }

    public void setMENU_ID(BigDecimal MENU_ID) {
        this.MENU_ID = MENU_ID;
    }
}
