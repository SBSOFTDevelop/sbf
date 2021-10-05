package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.gxt.components.VerticalFieldSet;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.FormScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ru.sbsoft.client.utils.VLC;

/**
 * Панель для полей данных на форме с функцией форматирования полей и их меток в
 * две колонки.
 */
public class SimplePageFormContainer extends VerticalLayoutContainer {

    protected static int DEFAULT_LABEL_WIDTH = 100;

    private int labelWidth;

    public SimplePageFormContainer() {
        this(true);
    }

    public SimplePageFormContainer(int labelWidth) {
        this(labelWidth, true);
    }

    public SimplePageFormContainer(boolean initScroll) {
        this(DEFAULT_LABEL_WIDTH, initScroll);
    }

    public SimplePageFormContainer(int labelWidth, boolean initScroll) {
        super();
        this.labelWidth = labelWidth;
        if (initScroll) {
            setAdjustForScroll(true);
            setScrollSupport(new FormScrollSupport(getElement()));
            setScrollMode(ScrollSupport.ScrollMode.AUTOY);
        }
    }

    public int getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
        updateLabelsWidth();
    }

    private void updateLabelsWidth() {
        updateLabelsWidth(this);
    }

    private void updateLabelsWidth(HasWidgets container) {
        SbfFieldHelper.updateLabelsWidth(container, labelWidth);
    }

    public void updateLabels() {
        updateLabelsWidth();
        updateLabelsAlign();

        for (VerticalFieldSet fset : getFieldsetsFromTab(this, null)) {
            fset.updateLabelsWidth();
        }

        forceLayout();
    }

    private void updateLabelsAlign() {
        updateLabelsAlign(this);
    }

    private void updateLabelsAlign(HasWidgets container) {
        SbfFieldHelper.updateLabelsAlign(container);
    }

    @Override
    public void add(IsWidget child) {
        super.add(child);
    }

    @Override
    public void add(Widget child) {
        super.add(child);
    }

    @Override
    public void add(IsWidget child, VerticalLayoutData layoutData) {
        super.add(child, layoutData);
    }

    public void addFieldSet(final IsWidget child) {
        addFieldSet(child, false);
    }

    public void addFieldSet(final IsWidget child, boolean isResizable) {
        if (null != child) {

            add(child, isResizable ? VLC.FILL : VLC.CONST);
        }
    }

    @Override
    protected void onInsert(int index, Widget child) {

        if (child instanceof FieldSet && child.getLayoutData() == VLC.CONST) {
            child.setLayoutData(VLC.CONST_M3);
        }

        super.onInsert(index, child);
    }

    private List<VerticalFieldSet> getFieldsetsFromTab(HasWidgets container, List<VerticalFieldSet> list) {
        if (list == null) {
            list = new ArrayList<VerticalFieldSet>();
        }

        Iterator<Widget> it = container.iterator();
        while (it.hasNext()) {
            Widget w = it.next();

            if (w instanceof VerticalFieldSet) {
                list.add((VerticalFieldSet) w);
            }

            if (w instanceof HasWidgets) {
                getFieldsetsFromTab((HasWidgets) w, list);
            }
        }
        return list;
    }
}
