package ru.sbsoft.operation.kladr;

import ru.sbsoft.common.Strings;

/**
 * @author balandin
 * @since Mar 15, 2013 1:00:00 PM
 */
public class KDHouseItem {

    public enum HouseType {

        SINGLE, MULTY, EVEN, ODD, ERROR
    }
    private String text;
    private int begNumber = 0;
    private int endNumber = 0;
    private HouseType type;

    public KDHouseItem(String text) {
        if (Strings.isEmpty(text)) {
            type = HouseType.ERROR;
            return;
        }
        this.text = text.trim().toUpperCase();
        int posSpace = this.text.indexOf('-');
        // диапазон ----------------------------------------------------------
        if (posSpace > 0) {
            try {
                begNumber = getHouseNumber(this.text.substring(0, posSpace));
            } catch (NumberFormatException ex) {
                type = HouseType.ERROR;
            }
            try {
                endNumber = getHouseNumber(this.text.substring(posSpace + 1));
            } catch (NumberFormatException ex) {
                type = HouseType.ERROR;
            }
            if (this.text.charAt(0) == 'Ч') {
                type = HouseType.EVEN;
            } else if (this.text.charAt(0) == 'Н') {
                type = HouseType.ODD;
            } else {
                type = HouseType.MULTY;
            }
        } // одиночное значение ------------------------------------------------ 
        else {
            try {
                type = HouseType.SINGLE;
                begNumber = getHouseNumber(this.text);
            } catch (NumberFormatException ex) {
                //ничего не делаем - дом составной
            }
        }
    }

    public KDHouseItem(String text, int begNumber, int endNumber, HouseType type) {
        this.text = text.trim().toUpperCase();
        this.begNumber = begNumber;
        this.endNumber = endNumber;
        this.type = type;
    }

    private int getHouseNumber(final String st) throws NumberFormatException {
        int b = -1;
        int e = 0;
        for (int i = 0; i < st.length(); i++) {
            char ch = st.charAt(i);
            if (ch >= '0' && ch <= '9') {
                if (-1 == b) {
                    b = i;
                }
                e = i;
            }
        }
        if (-1 == b) {
            throw new NumberFormatException(st);
        }
        return Integer.parseInt(st.substring(b, e + 1));
    }

    public String getText() {
        return text;
    }

    public int getBegNumber() {
        return begNumber;
    }

    public int getEndNumber() {
        return endNumber;
    }

    public HouseType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.text != null ? this.text.hashCode() : 0);
        hash = 79 * hash + this.begNumber;
        hash = 79 * hash + this.endNumber;
        hash = 79 * hash + (this.type != null ? this.type.hashCode() : 0);
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
        final KDHouseItem other = (KDHouseItem) obj;
        if ((this.text == null) ? (other.text != null) : !this.text.equals(other.text)) {
            return false;
        }
        if (this.begNumber != other.begNumber) {
            return false;
        }
        if (this.endNumber != other.endNumber) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
}
