package ru.sbsoft.shared;

import java.io.Serializable;

/**
 * Возвращает результат отправки EMail.
 * @see EMailDaoBean
 * @author Sokoloff
 */
public class EMailResult implements Serializable {
    
    private ResultEnum result;
    private String error;

    public EMailResult() {
    }
    
    public EMailResult(ResultEnum result, String error) {
        this.result = result;
        this.error = error;
    }

    public ResultEnum getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public void setResult(ResultEnum result) {
        this.result = result;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    public static enum ResultEnum {
        OK,
        ERROR;
    }

}
