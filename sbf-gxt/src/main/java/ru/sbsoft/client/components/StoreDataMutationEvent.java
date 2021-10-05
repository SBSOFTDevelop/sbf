package ru.sbsoft.client.components;

import com.google.gwt.event.shared.GwtEvent;
import com.sencha.gxt.data.shared.event.StoreEvent;
import java.util.Collections;
import java.util.List;

/**
 * Indicates that in the Store has been changed, and is visible under the current filter settings.
 *
 * @param <M> the model type
 */
public class StoreDataMutationEvent<M> extends StoreEvent<M, StoreDataMutationHandler<M>> {
    
    public static enum MutationType {ADD, REMOVE, CLEAR, UPDATE, DATA_CHANGE}

    private static GwtEvent.Type<StoreDataMutationHandler<?>> TYPE;

    /**
     * Gets the type associated with this event.
     *
     * @return returns the handler type
     */
    public static GwtEvent.Type<StoreDataMutationHandler<?>> getType() {
        if (TYPE == null) {
            TYPE = new GwtEvent.Type<StoreDataMutationHandler<?>>();
        }
        return TYPE;
    }

    private final M parent;

    private final List<M> items;

    private final int index;
    
    private final MutationType mutationType;

    /**
     * Creates a new store data mutation event.
     *
     * @param parent the parent of the changed data
     * @param index the mutate index
     * @param items the items that were mutated
     */
    public StoreDataMutationEvent(M parent, int index, List<M> items, MutationType mutationType) {
        this.parent = parent;
        this.index = index;
        this.items = items == null || items.isEmpty() ? Collections.<M>emptyList() : items.size() == 1 ? Collections.singletonList(items.get(0)) : Collections.unmodifiableList(items);
        this.mutationType = mutationType;
    }

    /**
     * Creates a new store data mutation event.
     *
     * @param parent the parent of the changed data
     * @param index the mutate index
     * @param item the item that were mutated
     */
    public StoreDataMutationEvent(M parent, int index, M item, MutationType mutationType) {
        this.parent = parent;
        this.index = index;
        this.items = item != null ? Collections.singletonList(item) : Collections.<M>emptyList();
        this.mutationType = mutationType;
    }

    /**
     * Creates a new store data mutation event.
     *
     * @param index the insert index
     * @param items the items that were added
     */
    public StoreDataMutationEvent(int index, List<M> items, MutationType mutationType) {
        this(null, index, items, mutationType);
    }

    /**
     * Creates a new data mutation add event.
     *
     * @param index the insert index
     * @param item the item that was added
     */
    public StoreDataMutationEvent(int index, M item, MutationType mutationType) {
        this(null, index, item, mutationType);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GwtEvent.Type<StoreDataMutationHandler<M>> getAssociatedType() {
        return (GwtEvent.Type) getType();
    }

    /**
     * Returns the insert index for this store mutation.
     *
     * @return the mutate index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the items that were mutated for this store add event.
     *
     * @return the list of items that were mutated
     */
    public List<M> getItems() {
        return items;
    }
    
    public int getItemCount(){
        return items != null ? items.size() : 0;
    }

    /**
     * Returns the parent model who's children were just loaded. Parent will always be null with ListStore.
     *
     * @return the parent or null if ListStore
     */
    public M getParent() {
        return parent;
    }

    @Override
    protected void dispatch(StoreDataMutationHandler<M> handler) {
        handler.onDataMutation(this);
    }

    public MutationType getMutationType() {
        return mutationType;
    }
    
    
    
}
