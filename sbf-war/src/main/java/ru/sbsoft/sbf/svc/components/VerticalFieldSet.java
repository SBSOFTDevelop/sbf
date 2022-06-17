package ru.sbsoft.sbf.svc.components;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.core.client.util.Padding;
import ru.sbsoft.svc.widget.core.client.container.MarginData;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import ru.sbsoft.svc.widget.core.client.form.CheckBox;
import ru.sbsoft.svc.widget.core.client.form.DualListField;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import ru.sbsoft.svc.widget.core.client.form.FieldSet;
import ru.sbsoft.svc.widget.core.client.form.FormPanelHelper;
import ru.sbsoft.svc.widget.core.client.form.HtmlEditor;
import ru.sbsoft.sbf.svc.utils.VLC;

/**
 * Набор полей, расположенных вертикально.
 *
 * @author Kiselev
 */
public class VerticalFieldSet extends FieldSet {

    private final static VerticalLayoutData VLC_B5 = new VerticalLayoutData(1, 0.95, new Margins(0, 0, 3, 0));
    private final static Padding FLD_PADDING = new Padding(3, 4, 0, 4);

    final private VerticalLayoutContainer vContainer = new VerticalLayoutContainer();
    private int labelWidth = -1;
    private boolean expandFirst = true;

    public VerticalFieldSet() {
        super();
        setWidget(vContainer);
        getElement().setPadding(FLD_PADDING);
        //Исправление ошибки ресайза вложенных элементов при раскрытии сета
        addExpandHandler(event -> {
            if (expandFirst) {
                expandFirst = false;
                this.setWidth("800px");
            }
        });
    }

    public VerticalFieldSet(String caption) {
        this();
        setHeading(caption);
    }

    @Override
    public void forceLayout() {
        //vContainer.forceLayout();
        //super.forceLayout();
        
        doLayout();

    }
    
    @Override
    public final void setHeading(String heading) {
        if (heading == null || "".equals(heading)) {
            super.setHeading("");
            return;
        }
        final StringBuffer b = new StringBuffer(heading);
        b.setCharAt(0, b.substring(0, 1).toUpperCase().charAt(0));
        b.insert(0, ' ');
        b.append(' ');
        super.setHeading(b.toString());
    }

    public void addField(IsWidget child) {
        addField(child, VLC.CONST);
    }

    public void addField(IsWidget child, MarginData layoutData) {
        if (!(layoutData instanceof VerticalLayoutData)) {
            throw new UnsupportedOperationException();
        }
        if (null == child) {
            return;
        }
        if (child instanceof FieldLabel) {
            FieldLabel l = (FieldLabel) child;
            if (l.getWidget() instanceof CheckBox) {
                vContainer.add(child);
                return;
            }
        } else if (child instanceof DualListField || child instanceof HtmlEditor) {

            vContainer.add(child, VLC_B5);
            return;

        }

        vContainer.add(child, (VerticalLayoutData) layoutData);

    }

    @Override
    public void add(IsWidget child) {
        addField(child);
    }

    @Override
    public void add(Widget child) {
        addField(child);
    }

    @Override
    public void add(Widget child, MarginData layoutData) {
        addField(child, layoutData);
    }

    @Override
    public void clear() {
        vContainer.clear();
    }

    @Override
    public Widget getWidget(int index) {
        return vContainer.getWidget(index);
    }

    @Override
    public int getWidgetCount() {
        return vContainer.getWidgetCount();
    }

    @Override
    public int getWidgetIndex(IsWidget child) {
        return vContainer.getWidgetIndex(child);
    }

    @Override
    public int getWidgetIndex(Widget child) {
        return vContainer.getWidgetIndex(child);
    }

    @Override
    public boolean remove(int index) {
        return vContainer.remove(index);
    }

    @Override
    public boolean remove(IsWidget child) {
        return vContainer.remove(child);
    }

    @Override
    public boolean remove(Widget child) {
        return vContainer.remove(child);
    }

    public int getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
        updateLabelsWidth();
    }

    public void updateLabelsWidth() {
        if (labelWidth < 0) {
            return;
        }
        for (FieldLabel label : FormPanelHelper.getFieldLabels(this)) {
            label.setLabelWidth(labelWidth);
        }

    }
    
}
