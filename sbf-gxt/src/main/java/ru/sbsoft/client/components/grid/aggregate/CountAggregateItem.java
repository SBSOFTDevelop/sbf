package ru.sbsoft.client.components.grid.aggregate;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.sencha.gxt.widget.core.client.form.TextField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.client.components.grid.dlgbase.ItemBase;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 */
public class CountAggregateItem extends ItemBase {

    private final TextField result;

    public CountAggregateItem() {
        setCaption(I18n.get(SBFBrowserStr.labelAggregatesRowCount));
        add(result = new TextField(), Item.MARGINS);
        result.setReadOnly(true);
        //--- TextField right text align code (somehow not works)
        NodeList<Element> inpNodes = result.getElement().getElementsByTagName("input");
        if (inpNodes != null && inpNodes.getLength() > 0) {
            Style style = inpNodes.getItem(0).getStyle();
            style.setProperty("textAlign", "right");
        }
        //---
    }

    public void setCount(long l) {
        result.setValue(Long.toString(l));
    }

    @Override
    public void clearValue() {
        super.clearValue();
        result.setValue(null);
    }

}
