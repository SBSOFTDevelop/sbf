package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import ru.sbsoft.client.utils.VLC;

/**
 * Панель для полей данных на форме с функцией форматирования полей и их меток в
 * две колонки.
 * Устарела. Используется только в {@link ru.sbsoft.client.components.kladr.AddressEditForm}
 * @deprecated используйте {@link SimplePageFormContainer}
 * @author balandin
 * @since Mar 15, 2013 7:22:13 PM
 */
public class SBSoftFieldSetContainer extends VerticalLayoutContainer {

    private static int DEFAULT_LABEL_WIDTH = 200;
    private final int labelWidth;

    public SBSoftFieldSetContainer() {
        this(DEFAULT_LABEL_WIDTH);
    }

    public SBSoftFieldSetContainer(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    public void add(final FieldLabel fieldLabel, boolean widthAsIs) {
        add(labelWidth, fieldLabel, widthAsIs);
    }

    public void add(int labelWidth, final FieldLabel fieldLabel, boolean widthAsIs) {
        fieldLabel.setLabelWidth(labelWidth);
        if (widthAsIs) {
            super.add(fieldLabel);
        } else {
            super.add(fieldLabel, VLC.CONST);
        }
    }

    public void add(final FieldLabel fieldLabel) {
        add(fieldLabel, false);
    }

    public void add(final FieldLabel fieldLabel, int width) {
        fieldLabel.setLabelWidth(labelWidth);
        fieldLabel.getWidget().setWidth(width + "px");
        super.add(fieldLabel);
    }

    public void addFill(final FieldLabel fieldLabel) {
        fieldLabel.setLabelWidth(labelWidth);
        add(fieldLabel, VLC.FILL);
    }

    public int getLabelWidth() {
        return labelWidth;
    }
}
