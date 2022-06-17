/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.svc.widget.core.client.container;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.util.Size;

/**
 * SimpleContainer takes one child and sizes it to match the container size.
 *
 * <p/>
 * Code Snippet:
 *
 * <pre>
 * public void onModuleLoad() {
 * SimpleContainer c = new SimpleContainer();
 * c.add(new Label("Hello world"));
 * RootPanel.get().add(c);
 * }
 * </pre>
 */
public class SimpleContainer extends ResizeContainer implements HasOneWidget {

    protected Widget widget;
    protected boolean resize = true;

    private static Logger logger = Logger.getLogger(SimpleContainer.class.getName());

    /**
     * Creates a simple container.
     */
    public SimpleContainer() {
        this(false);
    }

    protected SimpleContainer(boolean deferElement) {
        if (!deferElement) {
            setElement(Document.get().createDivElement());
        }
    }

    @Override
    public void add(Widget child) {
        setWidget(child);
    }

    /**
     * Adds a widget to the simple layout container with the specified layout
     * parameters.
     *
     * @param child the widget to add to the layout container
     * @param layoutData the parameters that describe how to lay out the widget
     */
    @UiChild(limit = 1, tagname = "child")
    public void add(Widget child, MarginData layoutData) {
        child.setLayoutData(layoutData);
        setWidget(child);
    }

    @Override
    public Widget getWidget() {
        return widget;
    }

    /**
     * Returns true if the child widget is resized to container size.
     *
     * @return true if resizing
     */
    public boolean isResize() {
        return resize;
    }

    @Override
    protected void setElement(Element elem) {
        super.setElement(elem);
        //EXTGWT-3759
        // margins on the child widgets can cause the container element to be offset by the margin size
        // overflow is set in the widget directly, not the appearance, to ensure overflow is set
        // overflow is set after setElement to allow the value to be changed after construction and before
        // the component is attached
        getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
    }

    /**
     * True to resize the child widget to match the container size (defaults to
     * true).
     *
     * @param resize true to resize
     */
    public void setResize(boolean resize) {
        this.resize = resize;
    }

    @Override
    public void setWidget(IsWidget w) {
        setWidget(asWidgetOrNull(w));
    }

    @Override
    @UiChild(limit = 1, tagname = "widget")
    public void setWidget(Widget w) {
        if (w != widget) {
            if (widget != null) {
                widget.removeFromParent();
            }
            widget = w;
            if (widget != null) {
                insert(widget, 0);
            }
        }
    }

    @Override
    protected void doLayout() {
        if (widget != null && resize) {
            Size size = getContainerTarget().getStyleSize();

            if (SVCLogConfiguration.loggingIsEnabled()) {
                logger.finest(getId() + " doLayout  size: " + size);
            }

            int width = -1;
            if (!isAutoWidth()) {
                width = size.getWidth() - getLeftRightMargins(widget);
            }
            int height = -1;
            if (!isAutoHeight()) {
                height = size.getHeight() - getTopBottomMargins(widget);
            }
            applyLayout(widget, width, height);
        }
    }

    @Override
    protected void onRemove(Widget child) {
        super.onRemove(child);
        if (widget == child) {
            widget = null;
        }
    }
}
