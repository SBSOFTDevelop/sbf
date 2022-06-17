package ru.sbsoft.client.components.field;

import com.google.gwt.user.client.ui.InlineHTML;
import ru.sbsoft.client.components.form.CustomField;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.sbf.svc.components.FieldsContainer;

/**
 *
 * @author sokolov
 */
public class FileLnkField extends CustomField<String> {

    private final InlineHTML lnk;
    private boolean readOnly;

    private static final String BEGIN_REF = "<a href=\"";

    public FileLnkField() {
        super();
        lnk = new InlineHTML();
        FieldsContainer container = new FieldsContainer();
        container.add(lnk, HLC.FILL);

        setWidget(container);
    }

    private void setFileUrl(String url) {
        setFileUrl(url, null);
    }
    
    public void setFileUrl(String url, String fname) {
        if (url == null || url.equals("null") || url.isEmpty()) {
            lnk.setHTML((String) null);
            return;
        }
        if (fname == null) {
            fname = url.substring(url.lastIndexOf('/') + 1);
        }
        lnk.setHTML(BEGIN_REF + url + "\">" + fname + "</a>");
    }
    
    private String getClearUrl(String value) {
        return value != null ? value.substring(value.lastIndexOf("\">") + 2).replaceAll("</a>", "") : null;
    }

    @Override
    public void setValue(String value) {
        setFileUrl(value);
    }

    @Override
    public String getValue() {
        String res = lnk.getHTML();

        return res == null || !res.startsWith(BEGIN_REF) ? null : getClearUrl(res);
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    
}
