/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @param <T> the type that this Cell represents
 * @param <C> the cell type
 *
 * @author vk
 */
public class CellChangeEvent<T, C extends Cell<T>> extends GwtEvent<CellChangeEvent.CellChangeHandler<T, C>> {

    /**
     * Handler type.
     */
    private static Type<CellChangeHandler<?, ?>> TYPE;

    /**
     * Fires a value change event on all registered handlers in the handler
     * manager. If no such handlers exist, this method will do nothing.
     *
     * @param <T> the type that this Cell represents
     * @param <C> the cell type
     * @param source the source of the handlers
     * @param cell the cell
     */
    public static <T, C extends Cell<T>> void fire(HasCellChangeHandlers<T, C> source, C cell) {
        if (TYPE != null) {
            CellChangeEvent<T, C> event = new CellChangeEvent<>(cell);
            source.fireEvent(event);
        }
    }

    /**
     * Fires value change event if the old cell is not equal to the new cell.
     * Use this call rather than making the decision to short circuit yourself for
     * safe handling of null.
     *
     * @param <T> the type that this Cell represents
     * @param <C> the cell type
     * @param source the source of the handlers
     * @param oldCell the oldCell, may be null
     * @param newCell the newCell, may be null
     */
    public static <T, C extends Cell<T>> void fireIfNotEqual(HasCellChangeHandlers<T, C> source, C oldCell, C newCell) {
        if (shouldFire(source, oldCell, newCell)) {
            CellChangeEvent<T, C> event = new CellChangeEvent<>(newCell);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static Type<CellChangeHandler<?, ?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    /**
     * Convenience method to allow subtypes to know when they should fire a cell
     * change event in a null-safe manner.
     *
     * @param <T> the type that this Cell represents
     * @param <C> the cell type
     * @param source the source
     * @param oldCell the old cell
     * @param newCell the new cell
     * @return whether the event should be fired
     */
    protected static <T, C extends Cell<T>> boolean shouldFire(HasCellChangeHandlers<T, C> source, C oldCell, C newCell) {
        return TYPE != null && oldCell != newCell;
    }

    private final C cell;

    /**
     * Creates a cell change event.
     *
     * @param cell the cell
     */
    protected CellChangeEvent(C cell) {
        this.cell = cell;
    }

    // The instance knows its CellChangeHandler is of type C, but the TYPE
    // field itself does not, so we have to do an unsafe cast here.
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public final Type<CellChangeHandler<T, C>> getAssociatedType() {
        return (Type) TYPE;
    }

    /**
     * Gets the cell.
     *
     * @return the cell
     */
    public C getCell() {
        return cell;
    }

    @Override
    public String toDebugString() {
        return super.toDebugString() + getCell();
    }

    @Override
    protected void dispatch(CellChangeHandler<T, C> handler) {
        handler.onCellChange(this);
    }

    public interface HasCellChangeHandlers<T, C extends Cell<T>> extends HasHandlers {

        /**
         * Adds a {@link CellChangeEvent} handler.
         *
         * @param handler the handler
         * @return the registration for the event
         */
        HandlerRegistration addCellChangeHandler(CellChangeHandler<T, C> handler);
    }

    public interface CellChangeHandler<T, C extends Cell<T>> extends EventHandler {

        /**
         * Called when {@link CellChangeEvent} is fired.
         *
         * @param event the {@link CellChangeEvent} that was fired
         */
        void onCellChange(CellChangeEvent<T, C> event);
    }
}
