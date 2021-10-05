package ru.sbsoft.client.components.form;

import com.sencha.gxt.widget.core.client.form.TextField;
import java.text.ParseException;

/**
 *
 * @author Kiselev
 */
public class TrimTextField extends TextField {

    @Override
    public String getText() {
        return trim(super.getText());
    }

    @Override
    public void setValue(String value) {
        super.setValue(trim(value));
    }

    @Override
    public void setText(String text) {
        super.setText(trim(text));
    }

    @Override
    public String getValueOrThrow() throws ParseException {
        return norm(super.getValueOrThrow());
    }

    @Override
    public String getValue() {
        return norm(super.getValue());
    }

    private String trim(String s) {
        return trimAdvanced(s);
    }

    private static String trimAdvanced(String value) {

        if (value == null) {
            return null;
        }

        int strLength = value.length();
        int len = value.length();
        int st = 0;
        char[] val = value.toCharArray();

        if (strLength == 0) {
            return "";
        }

        while ((st < len) && (val[st] <= ' ') || (val[st] == '\u00A0')) {
            st++;
            if (st == strLength) {
                break;
            }
        }
        while ((st < len) && (val[len - 1] <= ' ') || (val[len - 1] == '\u00A0')) {
            len--;
            if (len == 0) {
                break;
            }
        }

        return (st > len) ? "" : ((st > 0) || (len < strLength)) ? value.substring(st, len) : value;
    }

    private String norm(String s) {
        return s != null && !(s = s.trim()).isEmpty() ? s : null;
    }

}
