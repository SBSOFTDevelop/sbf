package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import ru.sbsoft.shared.FiltersBean;

/**
 * Сообытие изменения фильтров таблицы
 * @author panarin
 */
public class ChangeFiltersEvent extends GwtEvent<ChangeFiltersEvent.ChangeFiltersHandler> {

    public interface ChangeFiltersHandler extends EventHandler {

        void onChangeFilters(ChangeFiltersEvent event);
    }
    private static Type<ChangeFiltersHandler> TYPE;
    private FiltersBean filters;

    public ChangeFiltersEvent(FiltersBean filters) {
        this.filters = filters;
    }

    public static Type<ChangeFiltersHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<ChangeFiltersHandler>();
        }
        return TYPE;
    }

    @Override
    public Type<ChangeFiltersEvent.ChangeFiltersHandler> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(ChangeFiltersHandler handler) {
        handler.onChangeFilters(this);
    }

    public FiltersBean getFilters() {
        return filters;
    }

    public void setFilters(FiltersBean filters) {
        this.filters = filters;
    }
}
