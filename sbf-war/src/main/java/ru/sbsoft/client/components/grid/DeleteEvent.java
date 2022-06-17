package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import java.math.BigDecimal;
import ru.sbsoft.client.components.grid.DeleteEvent.DeleteHandler;

/**
 * Событие удаления строки данных из таблицы {@link ru.sbsoft.client.components.grid.SystemGrid}
 * @author balandin
 * @since Nov 28, 2013 11:42:51 AM
 */
public class DeleteEvent extends GwtEvent<DeleteHandler> {

    private final BigDecimal recordID;

    public interface DeleteHandler extends EventHandler {

        void onDelete(DeleteEvent event);
    }
    private static Type<DeleteHandler> TYPE;

    public DeleteEvent(BigDecimal recordID) {
        this.recordID = recordID;
    }

    public BigDecimal getRecordID() {
        return recordID;
    }

    public static Type<DeleteHandler> getType() {
        return TYPE != null ? TYPE : (TYPE = new Type<DeleteHandler>());
    }

    @Override
    public Type<DeleteHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(DeleteHandler handler) {
        handler.onDelete(this);
    }
}
