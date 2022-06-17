package ru.sbsoft.client.components.grid.dlgbase;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.VLD;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;

/**
 *
 * @author Kiselev
 */
public class Caption extends Unit {

    public Caption(I18nResourceInfo captionInf, String... parameters) {
        this(I18n.get(captionInf, parameters));
    }
    
    public Caption(String caption) {
        Label l = new Label(caption);
        l.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        l.getElement().getStyle().setPaddingLeft(4, Style.Unit.PX);

        SimpleContainer cnt = new SimpleContainer();
        cnt.setWidget(l);

        add(cnt, HLC.FILL);
        setLayoutData(new VLD(1, -1, new Margins(1, 1, 1, 1)));
        setHeight(18);
    }

    @Override
    public int getHeight() {
        return super.getHeight() + 2;
    }
}
