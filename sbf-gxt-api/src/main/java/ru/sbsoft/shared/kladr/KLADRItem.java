package ru.sbsoft.shared.kladr;

import java.io.Serializable;
import ru.sbsoft.common.Strings;

/**
 * @author balandin
 * @since Mar 15, 2013 12:42:21 PM
 */
public class KLADRItem implements Serializable {

    private String name;
    private String prefix;
    private String postcode;
    //
    private String code;
    private String status;
    private boolean actual;
    //
    private transient boolean userInput = false;

    public KLADRItem() {
    }

    public KLADRItem(String prefix, String name, String postcode) {
        this.prefix = prefix;
        this.name = name;
        this.postcode = postcode;
    }

    public static KLADRItem createStub(String name) {
        final KLADRItem item = new KLADRItem(null, name, null);
        item.setUserInput(true);
        return item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public String getFullName() {
        return name + (prefix == null ? "" : " " + prefix);
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KLADRItem other = (KLADRItem) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.prefix == null) ? (other.prefix != null) : !this.prefix.equals(other.prefix)) {
            return false;
        }
        if ((this.postcode == null) ? (other.postcode != null) : !this.postcode.equals(other.postcode)) {
            return false;
        }
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        if ((this.status == null) ? (other.status != null) : !this.status.equals(other.status)) {
            return false;
        }
        if (this.actual != other.actual) {
            return false;
        }
        if (this.userInput != other.userInput) {
            return false;
        }
        return true;
    }

    public void setUserInput(boolean userInput) {
        this.userInput = userInput;
    }

    public boolean isUserInput() {
        return userInput;
    }

    public boolean checkAddress(String value) {
        if (value == null) {
            return false;
        }

        boolean oneWord = value.indexOf(" ") == -1;
        value = normalize(value);

        if (oneWord) {
            String testName = normalize(name);
            if (value.equals(testName)) {
                return true;
            }
            return false;
        }

        String testName = normalize(getFullName());
        if (value.equals(testName)) {
            return true;
        }
        if (prefix == null) {
            return false;
        }
        testName = normalize(prefix + " " + name);
        if (value.equals(testName)) {
            return true;
        }
        return false;
    }

    private static String normalize(String value) {
        return Strings.clean(value, true).toLowerCase().replaceAll("\\.", "").replaceAll("\\s", "");
    }
}
