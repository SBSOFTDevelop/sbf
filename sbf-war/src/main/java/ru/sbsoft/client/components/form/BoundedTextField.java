package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.widget.core.client.form.TextField;

/**
 * Текстовое поле с ограничением длины.
 * @author balandin
 * @since Jun 25, 2013 5:49:43 PM
 */
public class BoundedTextField extends TextField {

    private int maxLength = -1;

    public BoundedTextField() {
        this(-1);
    }

    public BoundedTextField(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        if (isRendered()) {
            applyMasLength();
        }
    }

    @Override
    protected void onRedraw() {
        super.onRedraw();
        applyMasLength();
    }

    private void applyMasLength() {
        if (maxLength > 0) {
            getCell().getInputElement(getElement()).setMaxLength(maxLength);
        }
    }
}
