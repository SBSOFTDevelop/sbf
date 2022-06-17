package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import java.math.BigDecimal;
import ru.sbsoft.client.components.grid.InsertEvent.InsertHandler;

/**
 * Событие добавления строки данных из таблицы {@link ru.sbsoft.client.components.grid.SystemGrid}
 *
 * @author balandin
 * @since Nov 28, 2013 11:43:03 AM
 */
public class InsertEvent extends GwtEvent<InsertHandler> {

    private final BigDecimal recordID;

    public interface InsertHandler extends EventHandler {

        void onInsert(InsertEvent event);
    }
    private static Type<InsertHandler> TYPE;

    public InsertEvent(BigDecimal recordID) {
        this.recordID = recordID;
    }

    public BigDecimal getRecordID() {
        return recordID;
    }

    public static Type<InsertHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<InsertHandler>();
        }
        return TYPE;
    }

    @Override
    public Type<InsertHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(InsertHandler handler) {
        handler.onInsert(this);
    }
}
