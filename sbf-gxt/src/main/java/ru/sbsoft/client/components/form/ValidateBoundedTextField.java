package ru.sbsoft.client.components.form;

import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Текстовое поле с дополнительными проверками вводимого значения.
 * @author sotnikov
 */
public class ValidateBoundedTextField extends TextField {

	private int maxLength = -1;

	public ValidateBoundedTextField() {
		super(new ValidateTextInputCell(false));
	}

	/**
         * @param allowChars набор символов, которые можно вводить
         */
        public ValidateBoundedTextField(String allowChars) {
		super(new ValidateTextInputCell(allowChars));
	}
        
        /**
         * @param allowChars набор символов, которые можно вводить
         * @param maxLength максимальная длина строки
         */
	public ValidateBoundedTextField(String allowChars, int maxLength) {
		super(new ValidateTextInputCell(allowChars));
		this.maxLength = maxLength;
	}

        /**
         * @param maxLength максимальная длина строки
         */
	public ValidateBoundedTextField(int maxLength) {
		this(false, maxLength);
	}

        /**
         * @param onlyDigits флаг строки, состоящей из цифр
         * @param maxLength максимальная длина строки
         */
	public ValidateBoundedTextField(boolean onlyDigits, int maxLength) {
		super(new ValidateTextInputCell(onlyDigits));
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
