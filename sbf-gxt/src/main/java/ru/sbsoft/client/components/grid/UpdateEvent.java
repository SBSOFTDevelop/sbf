package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import java.math.BigDecimal;
import ru.sbsoft.client.components.grid.UpdateEvent.UpdateHandler;

/**
 * Событие изменения значений строки данных из таблицы {@link ru.sbsoft.client.components.grid.SystemGrid}
 *
 * @author balandin
 * @since Nov 28, 2013 11:43:15 AM
 */
public class UpdateEvent extends GwtEvent<UpdateHandler> {

    private final BigDecimal recordID;

    public interface UpdateHandler extends EventHandler {

        void onUpdate(UpdateEvent event);
    }
    private static Type<UpdateHandler> TYPE;

    public UpdateEvent(BigDecimal recordID) {
        this.recordID = recordID;
    }

    public BigDecimal getRecordID() {
        return recordID;
    }

    public static Type<UpdateHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<UpdateHandler>();
        }
        return TYPE;
    }

    @Override
    public Type<UpdateHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(UpdateHandler handler) {
        handler.onUpdate(this);
    }
}
