package ru.sbsoft.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.Window;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.GridView;
import java.util.Objects;
import ru.sbsoft.client.utils.CliUtil;

/**
 *
 * @author vk
 */
public class LastFocusMonitor {

    private static final int EVENT_MASK = Event.ONCLICK | Event.ONDBLCLICK | Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.KEYEVENTS | Event.ONFOCUS | Event.TOUCHEVENTS;

    private static LastFocusMonitor instance = null;

    public static void run() {
        if (instance == null) {
            instance = new LastFocusMonitor();
        }
    }

    public static LastFocusMonitor get() {
        return instance;
    }

    private Element lastActiveElement;
    private FocusItem focusItem;

    private LastFocusMonitor() {
        Event.addNativePreviewHandler(new Event.NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(Event.NativePreviewEvent ev) {
                Element activeElement = CliUtil.getActiveElement();
                if (focusItem != null && (ev.getTypeInt() & Event.KEYEVENTS) > 0 && isNoFocus(activeElement)) {
                    focusItem.doFocus();
                    NativeEvent old = ev.getNativeEvent();
                    String evType = (ev.getTypeInt() & Event.ONKEYDOWN) > 0 ? BrowserEvents.KEYDOWN : (ev.getTypeInt() & Event.ONKEYPRESS) > 0 ? BrowserEvents.KEYPRESS : BrowserEvents.KEYUP;
                    final NativeEvent newEv = Document.get().createKeyCodeEvent(evType, old.getCtrlKey(), old.getAltKey(), old.getShiftKey(), old.getMetaKey(), old.getKeyCode());
                    ev.cancel();
                    old.preventDefault();
                    old.stopPropagation();
                    Scheduler.get().scheduleDeferred(() -> CliUtil.getActiveElement().dispatchEvent(newEv));
                } else if ((ev.getTypeInt() & EVENT_MASK) > 0) {
                    if (activeElement != lastActiveElement && focusValid(activeElement)) {
                        lastActiveElement = activeElement;
                        Grid g = CliUtil.findParentAnyOf(CliUtil.findWidget(activeElement), Grid.class);
                        focusItem = g != null ? new GridFocusItem(g, activeElement) : new SimpleFocusItem(activeElement);
//                        debugFocusChange(activeElement, "=== FOCUS SAVED === ", ev);
                    }
                }
            }
        });
    }

    public void focusLastFocused() {
        if (focusItem != null) {
            focusItem.doFocus();
        }
    }

    private static boolean isNoFocus(Element activeElement) {
        Element foc = activeElement;
        Element pan = RootPanel.get().getElement();
        Element bod = RootPanel.getBodyElement();
        return foc == null || foc.equals(pan) || foc.equals(bod);
    }

    private static boolean focusValid(Element activeElement) {
        if (isNoFocus(activeElement)) {
            return false;
        }
        boolean inWnd = false;
        boolean inRoot = false;
        for (Widget w = CliUtil.findWidget(activeElement); !(w == null || inRoot); w = w.getParent()) {
            if (!inWnd && (w instanceof Window)) {
                inWnd = true;
            }
            if (w instanceof RootPanel) {
                inRoot = true;
            }
        }
        return inRoot && !inWnd;
    }
//
//    private static void debugFocusChange(Element activeElement, String msg, Event.NativePreviewEvent ev) {
//        if (ev != null) {
//            msg = new StringBuilder(msg).append(" Event: ").append(ev.getNativeEvent().getType()).toString();
//        }
//        DebugUtils.printInfo(activeElement, msg);
//    }
//
    private interface FocusItem {

        void doFocus();
    }

    private static class SimpleFocusItem implements FocusItem {

        private Element element;

        public SimpleFocusItem(Element activeElement) {
            Objects.requireNonNull(activeElement);
            this.element = activeElement;
        }

        @Override
        public void doFocus() {
            element.focus();
//            debugFocusChange(element, "=== SET FOCUS TO: ", null);
        }
    }

    private static class GridFocusItem implements FocusItem {

        private Grid grid;
        private final int rowIndex;
        private final int colIndex;

        public GridFocusItem(Grid grid, Element activeElement) {
            Objects.requireNonNull(grid);
            Objects.requireNonNull(activeElement);
            this.grid = grid;
            GridView v = grid.getView();
            this.rowIndex = v.findRowIndex(activeElement);
            this.colIndex = v.findCellIndex(activeElement, null);
//            DebugUtils.console("== Grid focus: row(" + rowIndex + ") col(" + colIndex + ")");
        }

        @Override
        public void doFocus() {
            int row = Math.min(Math.max(rowIndex, 0), grid.getStore().getAll().size() - 1);
            int col = colIndex;
            ColumnModel colMod = grid.getColumnModel();
            if (col < 0 || col >= colMod.getColumnCount() || colMod.isHidden(col)) {
                col = -1;
                for (int i = 0; col < 0 && i < colMod.getColumnCount(); i++) {
                    if (!colMod.isHidden(i)) {
                        col = i;
                    }
                }
            }
            if (row >= 0 && col >= 0) {
                grid.getView().focusCell(row, col, true);
            } else {
                CliUtil.focus(grid);
            }
//            DebugUtils.console("== SET FOCUS to Grid: row(" + row + ") col(" + col + ")");
        }
    }
}
