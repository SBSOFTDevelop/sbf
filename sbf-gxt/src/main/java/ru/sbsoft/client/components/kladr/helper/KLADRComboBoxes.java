package ru.sbsoft.client.components.kladr.helper;

import ru.sbsoft.client.components.kladr.KLADRItemComboBox;

/**
 * Список полей {@link KLADRItemComboBox}
 * @author balandin
 * @since Mar 24, 2015 12:35:07 PM
 */
public class KLADRComboBoxes {

    private KLADRItemComboBox[] items;
    private boolean onlyActual;

    public KLADRItemComboBox getPrev(KLADRItemComboBox item) {
        return get(find(item) - 1);
    }

    public KLADRItemComboBox getNext(KLADRItemComboBox item) {
        return get(find(item) + 1);
    }

    private int find(KLADRItemComboBox item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    private KLADRItemComboBox get(int index) {
        return (index >= 0 && index < items.length) ? items[index] : null;
    }

    public boolean actualStrict() {
        return onlyActual;
    }
    
    public KLADRItemComboBox[] getItems() {
        return items;
    }

    public void setItems(KLADRItemComboBox[] items) {
        this.items = items;
    }
    
    public boolean isOnlyActual() {
        return onlyActual;
    }

    public void setOnlyActual(boolean onlyActual) {
        this.onlyActual = onlyActual;
    }
}
