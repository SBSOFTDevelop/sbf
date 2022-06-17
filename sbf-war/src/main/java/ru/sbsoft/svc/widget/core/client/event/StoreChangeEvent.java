/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.svc.widget.core.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;
import ru.sbsoft.svc.data.shared.Store;

/**
 * @param <M> the Store model type
 * @param <S> the store type
 *
 * @author vk
 */
public class StoreChangeEvent<M, S extends Store<M>> extends GwtEvent<StoreChangeEvent.StoreChangeHandler<M, S>> {

    /**
     * Handler type.
     */
    private static Type<StoreChangeHandler<?, ?>> TYPE;

    /**
     * Fires a value change event on all registered handlers in the handler
     * manager. If no such handlers exist, this method will do nothing.
     *
     * @param <M> the ListStore model type
     * @param <S> the store type
     * @param source the source of the handlers
     * @param store the store
     */
    public static <M, S extends Store<M>> void fire(HasStoreChangeHandlers<M, S> source, S store) {
        if (TYPE != null) {
            StoreChangeEvent<M, S> event = new StoreChangeEvent<>(store);
            source.fireEvent(event);
        }
    }

    /**
     * Fires value change event if the old store is not equal to the new store.
     * Use this call rather than making the decision to short circuit yourself for
     * safe handling of null.
     *
     * @param <M> the ListStore model type
     * @param <S> the store type
     * @param source the source of the handlers
     * @param oldStore the oldStore, may be null
     * @param newStore the newStore, may be null
     */
    public static <M, S extends Store<M>> void fireIfNotEqual(HasStoreChangeHandlers<M, S> source, S oldStore, S newStore) {
        if (shouldFire(source, oldStore, newStore)) {
            StoreChangeEvent<M, S> event = new StoreChangeEvent<>(newStore);
            source.fireEvent(event);
        }
    }

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static Type<StoreChangeHandler<?, ?>> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    /**
     * Convenience method to allow subtypes to know when they should fire a store
     * change event in a null-safe manner.
     *
     * @param <M> the ListStore model type
     * @param <S> the store type
     * @param source the source
     * @param oldStore the old store
     * @param newStore the new store
     * @return whether the event should be fired
     */
    protected static <M, S extends Store<M>> boolean shouldFire(HasStoreChangeHandlers<M, S> source, S oldStore, S newStore) {
        return TYPE != null && oldStore != newStore;
    }

    private final S store;

    /**
     * Creates a store change event.
     *
     * @param store the store
     */
    protected StoreChangeEvent(S store) {
        this.store = store;
    }

    // The instance knows its ListStoreChangeHandler is of type C, but the TYPE
    // field itself does not, so we have to do an unsafe cast here.
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public final Type<StoreChangeHandler<M, S>> getAssociatedType() {
        return (Type) TYPE;
    }

    /**
     * Gets the store.
     *
     * @return the store
     */
    public S getStore() {
        return store;
    }

    @Override
    public String toDebugString() {
        return super.toDebugString() + getStore();
    }

    @Override
    protected void dispatch(StoreChangeHandler<M, S> handler) {
        handler.onStoreChange(this);
    }

    public interface HasStoreChangeHandlers<M, S extends Store<M>> extends HasHandlers {

        /**
         * Adds a {@link StoreChangeEvent} handler.
         *
         * @param handler the handler
         * @return the registration for the event
         */
        HandlerRegistration addStoreChangeHandler(StoreChangeHandler<M, S> handler);
    }

    public interface StoreChangeHandler<M, S extends Store<M>> extends EventHandler {

        /**
         * Called when {@link StoreChangeEvent} is fired.
         *
         * @param event the {@link StoreChangeEvent} that was fired
         */
        void onStoreChange(StoreChangeEvent<M, S> event);
    }
}
