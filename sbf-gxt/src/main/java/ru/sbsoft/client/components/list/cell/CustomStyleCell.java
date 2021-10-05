package ru.sbsoft.client.components.list.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.data.shared.ListStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.grid.style.CStyle;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author sychugin
 */
public class CustomStyleCell<M extends LookupInfoModel> extends AbstractCell<String> {

    private StyleChecker checker;

    public CustomStyleCell() {
        this.checker = new StyleChecker();
    }

    @Override
    public final void render(Context context, String value, SafeHtmlBuilder sb) {
        final String text = wrapText(Strings.isEmpty(value) ? "&#160;" : value);
        
        
        String style = checker.getStyle(context.getIndex());
        if (!style.isEmpty()) {
            sb.appendHtmlConstant("<div style= \"" + style + "\">" + text + "</div>");
        } else {
            SafeHtmlUtils.fromTrustedString(text);
        }
        
    }

    protected String wrapText(String text) {
        return text;
    }

    public final void setStores(final ListStore<M>... stores) {
        checker.setStores(Arrays.asList(stores));
    }

    public void onStyleCheck(M model, CStyle style) {
    }

    private class StyleChecker {

        private List<ListStore<M>> stores = new ArrayList<>();

        public String getStyle(int index) {
            SafeStylesBuilder safeStylesBuilder = new SafeStylesBuilder();

            for (ListStore<M> store : stores) {
                if (store != null && index < store.size()) {

                    M m = (M) store.get(index);
                    CStyle cs = new CStyle();
                    onStyleCheck(m, cs);

                    for (String s : cs.getCssStyles()) {
                        safeStylesBuilder.appendTrustedString(s);
                    }

                }
            }
            return safeStylesBuilder.toSafeStyles().asString();
        }

        public void onStyleCheck(M model, CStyle style) {
            CustomStyleCell.this.onStyleCheck(model, style);
        }

        public void setStores(List<ListStore<M>> stores) {
            this.stores.clear();
            this.stores.addAll(stores);
        }
    }

}
