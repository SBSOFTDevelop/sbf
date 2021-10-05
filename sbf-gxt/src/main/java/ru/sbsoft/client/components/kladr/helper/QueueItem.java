package ru.sbsoft.client.components.kladr.helper;

import ru.sbsoft.client.components.kladr.KLADRItemComboBox;

/**
 * @author balandin
 * @since Mar 24, 2015 12:32:30 PM
 */
public class QueueItem {

    private final KLADRItemComboBox field;
    private final String code;
    private final String name;

    public QueueItem(KLADRItemComboBox field, String code) {
        this(field, code, null);
    }

    public QueueItem(KLADRItemComboBox field, String code, String name) {
        this.field = field;
        this.code = code;
        this.name = name;
    }

    public KLADRItemComboBox getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
