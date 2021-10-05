package ru.sbsoft.client.components;

import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author Kiselev
 */
public abstract class StoreDataMutationHandler<M> implements StoreHandlers<M> {

    private final Set<StoreDataMutationEvent.MutationType> allowedTypes;

    public StoreDataMutationHandler() {
        allowedTypes = null;
    }

    public StoreDataMutationHandler(StoreDataMutationEvent.MutationType... types) {
        this((Set<StoreDataMutationEvent.MutationType>) (types != null && types.length > 0 ? EnumSet.copyOf(Arrays.asList(types)) : null));
    }

    public StoreDataMutationHandler(Set<StoreDataMutationEvent.MutationType> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    public abstract void onDataMutation(StoreDataMutationEvent<M> event);

    @Override
    public void onAdd(StoreAddEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.ADD)) {
            onDataMutation(new StoreDataMutationEvent<M>(event.getIndex(), event.getItems(), StoreDataMutationEvent.MutationType.ADD));
        }
    }

    @Override
    public void onRemove(StoreRemoveEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.REMOVE)) {
            onDataMutation(new StoreDataMutationEvent<M>(event.getIndex(), event.getItem(), StoreDataMutationEvent.MutationType.REMOVE));
        }
    }

    @Override
    public void onFilter(StoreFilterEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.DATA_CHANGE)) {
            onDataMutation(new StoreDataMutationEvent(-1, null, StoreDataMutationEvent.MutationType.DATA_CHANGE));
        }
    }

    @Override
    public void onClear(StoreClearEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.CLEAR)) {
            onDataMutation(new StoreDataMutationEvent(-1, null, StoreDataMutationEvent.MutationType.CLEAR));
        }
    }

    @Override
    public void onUpdate(StoreUpdateEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.UPDATE)) {
            onDataMutation(new StoreDataMutationEvent<M>(-1, event.getItems(), StoreDataMutationEvent.MutationType.UPDATE));
        }
    }

    @Override
    public void onDataChange(StoreDataChangeEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.DATA_CHANGE)) {
            onDataMutation(new StoreDataMutationEvent(-1, null, StoreDataMutationEvent.MutationType.DATA_CHANGE));
        }
    }

    @Override
    public void onRecordChange(StoreRecordChangeEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.UPDATE)) {
            onDataMutation(new StoreDataMutationEvent<M>(-1, Collections.singletonList(event.getRecord().getModel()), StoreDataMutationEvent.MutationType.UPDATE));
        }
    }

    @Override
    public void onSort(StoreSortEvent<M> event) {
        if (allowedTypes == null || allowedTypes.contains(StoreDataMutationEvent.MutationType.DATA_CHANGE)) {
            onDataMutation(new StoreDataMutationEvent(-1, null, StoreDataMutationEvent.MutationType.DATA_CHANGE));
        }
    }
}
