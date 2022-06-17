package ru.sbsoft.client.components.browser.filter.fields;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;

/**
 * @author balandin
 * @since Dec 16, 2014 1:07:09 PM
 */
public class FieldAccess extends AbstractSafeHtmlRenderer<CustomHolder> implements ModelKeyProvider<CustomHolder>, LabelProvider<CustomHolder> {

    public FieldAccess() {
    }

    @Override
    public String getKey(CustomHolder item) {
        return item.getKey();
    }

    @Override
    public String getLabel(CustomHolder item) {
        return item.getShortTitle();
    }

    @Override
    public SafeHtml render(CustomHolder item) {
        final String title = item.getComboItemTitle();
        if (item instanceof GroupHolder) {
            return SafeHtmlUtils.fromTrustedString("<span style='font-weight:bold'>" + title + "</span>");
        } else {
            return SafeHtmlUtils.fromTrustedString(title);
        }
    }
}
