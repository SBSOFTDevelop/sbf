package ru.sbsoft.client.components.grid;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.widget.core.client.event.GridEvent;

public class FrozenGridMouseEvent extends GridEvent<FrozenGridMouseEvent.MouseEventHandler> {
    private static Type<FrozenGridMouseEvent.MouseEventHandler> TYPE;
    private int rowIndex;
    private int cellIndex;
    private MouseEvent event;

    public static Type<FrozenGridMouseEvent.MouseEventHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type();
        }

        return TYPE;
    }
    public int getCellIndex() {
        return this.cellIndex;
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

    public MouseEvent getEvent() {
        return this.event;
    }

    public FrozenGridMouseEvent(int rowIndex, int cellIndex, MouseEvent event) {
        this.rowIndex = rowIndex;
        this.cellIndex = cellIndex;
        this.event = event;
    }

    @Override
    public Type<MouseEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(MouseEventHandler handler) {
        handler.onFrozenGridMouseEvent(this);
    }

    public interface MouseEventHandler extends EventHandler {
        void onFrozenGridMouseEvent(FrozenGridMouseEvent event);
    }

    public interface HasFrozenGridMouseEvent extends HasHandlers {
        HandlerRegistration addCellMouseDownHandler(FrozenGridMouseEvent.MouseEventHandler handler);
    }
}
