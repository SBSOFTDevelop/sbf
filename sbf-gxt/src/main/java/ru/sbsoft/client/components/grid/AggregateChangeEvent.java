package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import ru.sbsoft.client.components.grid.AggregateChangeEvent.AggregateChangeHandler;

/**
 * Сигнализирует об изменении обобщенных полей (aggregates), предназначенных для
 * расчета и отображения заданных обобщений по колонкам {@link SystemGrid}.
 *
 * @see ru.sbsoft.shared.meta.Aggregates
 * @author balandin
 * @since May 12, 2014 1:02:51 PM
 */
public class AggregateChangeEvent extends GwtEvent<AggregateChangeHandler> {

    public interface AggregateChangeHandler extends EventHandler {

        void onAggregateChange(AggregateChangeEvent event);
    }
    private static Type<AggregateChangeHandler> TYPE;

    public AggregateChangeEvent(SystemGrid grid) {
        setSource(grid);
    }

    public static Type<AggregateChangeHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<AggregateChangeHandler>();
        }
        return TYPE;
    }

    @Override
    public Type<AggregateChangeHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(AggregateChangeHandler handler) {
        handler.onAggregateChange(this);
    }

    public SystemGrid getSystemGrid() {
        return (SystemGrid) getSource();
    }
}
