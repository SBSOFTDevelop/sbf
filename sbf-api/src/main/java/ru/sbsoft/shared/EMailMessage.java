package ru.sbsoft.shared;

import java.io.Serializable;

/**
 * Класс инкапсулирует данные EMail.
 * @author Sokoloff
 */
public class EMailMessage implements Serializable {

    private String addressTo;
    private String addressFrom;
    private String subject;
    private String body;
    private boolean flagHiPriority;
    private boolean flagRecept;
    private boolean flagRead;

    public EMailMessage() {
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getAddressFrom() {
        return addressFrom;
    }

    public void setAddressFrom(String addressFrom) {
        this.addressFrom = addressFrom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isFlagHiPriority() {
        return flagHiPriority;
    }

    public void setFlagHiPriority(boolean flagHiPriority) {
        this.flagHiPriority = flagHiPriority;
    }

    public boolean isFlagRecept() {
        return flagRecept;
    }

    public void setFlagRecept(boolean flagRecept) {
        this.flagRecept = flagRecept;
    }

    public boolean isFlagRead() {
        return flagRead;
    }

    public void setFlagRead(boolean flagRead) {
        this.flagRead = flagRead;
    }

}
