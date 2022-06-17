package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Описывает структуру данных контектных переменных <PRODD.PD_VARIABLE>.
 *
 * @author Sokoloff
 */
public class ContextVariableModel implements Serializable {

    private BigDecimal ID;
    private String USER_LOGIN;
    private String VARIABLE_NAME;
    private Date VARIABLE_DATE;
    private String VARIABLE_STRING;
    private BigDecimal VARIABLE_NUMBER;

    public ContextVariableModel() {
    }

    public ContextVariableModel(String USER_LOGIN, String VARIABLE_NAME, Date VARIABLE_DATE, String VARIABLE_STRING, BigDecimal VARIABLE_NUMBER) {
        this.USER_LOGIN = USER_LOGIN;
        this.VARIABLE_NAME = VARIABLE_NAME;
        this.VARIABLE_DATE = VARIABLE_DATE;
        this.VARIABLE_STRING = VARIABLE_STRING;
        this.VARIABLE_NUMBER = VARIABLE_NUMBER;
    }

    public BigDecimal getID() {
        return ID;
    }

    public void setID(BigDecimal RECORD_UQ) {
        this.ID = RECORD_UQ;
    }

    public String getUSER_LOGIN() {
        return USER_LOGIN;
    }

    public void setUSER_LOGIN(String USER_ID) {
        this.USER_LOGIN = USER_ID;
    }

    public Date getVARIABLE_DATE() {
        return VARIABLE_DATE;
    }

    public void setVARIABLE_DATE(Date VARIABLE_DATE) {
        this.VARIABLE_DATE = VARIABLE_DATE;
    }

    public String getVARIABLE_NAME() {
        return VARIABLE_NAME;
    }

    public void setVARIABLE_NAME(String VARIABLE_ID) {
        this.VARIABLE_NAME = VARIABLE_ID;
    }

    public BigDecimal getVARIABLE_NUMBER() {
        return VARIABLE_NUMBER;
    }

    public void setVARIABLE_NUMBER(BigDecimal VARIABLE_NUMBER) {
        this.VARIABLE_NUMBER = VARIABLE_NUMBER;
    }

    public String getVARIABLE_STRING() {
        return VARIABLE_STRING;
    }

    public void setVARIABLE_STRING(String VARIABLE_STRING) {
        this.VARIABLE_STRING = VARIABLE_STRING;
    }
}
