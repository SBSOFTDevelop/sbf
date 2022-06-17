package ru.sbsoft.client.components.form;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.LocaleInfo;
import ru.sbsoft.svc.cell.core.client.form.TextInputCell;
import ru.sbsoft.svc.widget.core.client.event.XEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Текстовый визуальный объект  с дополнительными проверками вводимого значения.
 * @author sotnikov
 */
public class ValidateTextInputCell extends TextInputCell {

	protected String decimalSeparator = LocaleInfo.getCurrentLocale().getNumberConstants().decimalSeparator();
	protected List<Character> allowed;
	private int lastKeyCode;
	private String digitChars = "0123456789";
	private boolean allowOnlyDigits = false;

	/**
         * @param allowOnlyDigits флаг строки, состоящей из цифр
         */
        public ValidateTextInputCell(boolean allowOnlyDigits) {
		setOnlyDigits(allowOnlyDigits);
	}

        /**
         * @param allowChars набор символов, которые можно вводить
         */
	public ValidateTextInputCell(String allowChars) {
		setAllowedChars(allowChars);
	}

	public final void setAllowedChars(String baseChars) {
		allowed = new ArrayList<Character>();
		for (int i = 0; i < baseChars.length(); i++) {
			allowed.add(baseChars.charAt(i));
		}
	}

	public boolean isAllowOnlyDigits() {
		return allowOnlyDigits;
	}

	public final void setOnlyDigits(boolean allowOnlyDigits) {
		this.allowOnlyDigits = allowOnlyDigits;
		if (allowOnlyDigits) {
			allowed = new ArrayList<Character>();
			for (int i = 0; i < digitChars.length(); i++) {
				allowed.add(digitChars.charAt(i));
			}
		} else {
			allowed = null;
		}
	}

	@Override
	protected void onKeyDown(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdater) {
		super.onKeyDown(context, parent, value, event, valueUpdater);
		lastKeyCode = event.getKeyCode();
	}

	@Override
	protected void onKeyPress(Context context, Element parent, String value, NativeEvent event, ValueUpdater<String> valueUpdate) {
		super.onKeyPress(context, parent, value, event, valueUpdate);

		char key = (char) event.getCharCode();

		if (event.<XEvent>cast().isSpecialKey(lastKeyCode) || event.getCtrlKey()) {
			return;
		}

		if (allowed != null && !allowed.isEmpty() && !allowed.contains(key)) {
			event.stopPropagation();
			event.preventDefault();
		}
	}
}
