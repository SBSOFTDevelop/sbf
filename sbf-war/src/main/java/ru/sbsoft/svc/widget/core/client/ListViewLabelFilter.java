/**
 * Copyright (c) 2022 SBSOFT.
 * All rights reserved.
 */
package ru.sbsoft.svc.widget.core.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.Store;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent;
import ru.sbsoft.svc.data.shared.event.StoreClearEvent;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent;
import ru.sbsoft.svc.data.shared.event.StoreHandlers;
import ru.sbsoft.svc.data.shared.event.StoreRecordChangeEvent;
import ru.sbsoft.svc.data.shared.event.StoreRemoveEvent;
import ru.sbsoft.svc.data.shared.event.StoreSortEvent;
import ru.sbsoft.svc.data.shared.event.StoreUpdateEvent;
import ru.sbsoft.svc.widget.core.client.form.TextField;

/**
 *
 * @author vk
 * @param <M>
 * @param <N>
 */
public class ListViewLabelFilter<M, N> implements IsWidget {

    private final TextField field = new TextField();
    private final ValueProvider<N, String> labelProvider;
    private final Map<String, String> labelBuf = new HashMap<>();
    private ListView<M, N> view;
    private ListStore<M> store;
    private final LabelFilter storeFilter = new LabelFilter();
    private final StoreMonitor storeMonitor = new StoreMonitor();
    private HandlerRegistration storeMonitorRegistration;
    private HandlerRegistration storeChangeHandler;
    private String filterStr = null;
    private FilterChangeMonitor changeMonitor;

    public ListViewLabelFilter(ListView<M, N> view) {
        this(view, null);
    }

    public ListViewLabelFilter(ListView<M, N> view, ValueProvider<N, String> labelProvider) {
        this.view = view;
        this.labelProvider = labelProvider;
//        field.addKeyDownHandler(ev -> {
//            Scheduler.get().scheduleDeferred(() -> updateFilter());
//            logger.log(Level.SEVERE, "Key Down Event");
//        });
//        field.addKeyUpHandler(ev -> {
//            Scheduler.get().scheduleDeferred(() -> updateFilter());
//            logger.log(Level.SEVERE, "Key Up Event");
//        });
        field.addAttachHandler(ev -> {
            if (changeMonitor != null) {
                changeMonitor.stop();
                changeMonitor = null;
            }
            if (ev.isAttached()) {
                changeMonitor = new FilterChangeMonitor();
                Scheduler.get().scheduleFixedDelay(changeMonitor, 500);
            }
        });
        setStore(view.getStore());
        storeChangeHandler = view.addStoreChangeHandler(ev -> setStore(ev.getStore()));
    }

    public ValueProvider<N, String> getLabelProvider() {
        return labelProvider;
    }

    public final void setView(ListView<M, N> view) {
        if (this.storeChangeHandler != null) {
            this.storeChangeHandler.removeHandler();
            this.storeChangeHandler = null;
        }
        this.view = view;
        if (view != null) {
            setStore(view.getStore());
            storeChangeHandler = view.addStoreChangeHandler(ev -> setStore(ev.getStore()));
        } else {
            setStore(null);
        }
    }

    @Override
    public Widget asWidget() {
        return field;
    }

    public void clear() {
        labelBuf.clear();
        field.clear();
        updateFilter();
    }

    private void updateFilter() {
        updateFilterStr();
        if (store != null) {
            store.addFilter(storeFilter);
        }
    }

    private void updateFilterStr() {
        filterStr = field.getText();
        filterStr = filterStr != null && !(filterStr = filterStr.trim()).isEmpty() ? filterStr.toLowerCase() : null;
    }

    private void setStore(ListStore<M> store) {
        if (this.store != null) {
            this.store.removeFilter(storeFilter);
            this.store = null;
        }
        if (this.storeMonitorRegistration != null) {
            this.storeMonitorRegistration.removeHandler();
            this.storeMonitorRegistration = null;
        }
        clear();
        if (store != null) {
            this.storeMonitorRegistration = store.addStoreHandlers(storeMonitor);
            store.addFilter(storeFilter);
            store.setEnableFilters(true);
        }
        this.store = store;
    }

    private String getLabel(M m) {
        if (store != null) {
            return labelBuf.computeIfAbsent(store.getKeyProvider().getKey(m), k -> {
                N val = view.getValue(m);
                final String l = val != null ? labelProvider != null ? labelProvider.getValue(val) : val.toString() : "";
                return l != null ? l.toLowerCase() : "";
            });
        } else {
            return String.valueOf(m);
        }
    }

    private class LabelFilter implements Store.StoreFilter<M> {

        @Override
        public boolean select(Store<M> store, M parent, M item) {
            return filterStr == null || getLabel(item).contains(filterStr);
        }
    }

    private class StoreMonitor implements StoreHandlers<M> {

        @Override
        public void onAdd(StoreAddEvent<M> event) {
        }

        @Override
        public void onRemove(StoreRemoveEvent<M> event) {
            labelBuf.remove(event.getSource().getKeyProvider().getKey(event.getItem()));
        }

        @Override
        public void onFilter(StoreFilterEvent<M> event) {
        }

        @Override
        public void onClear(StoreClearEvent<M> event) {
            labelBuf.clear();
        }

        @Override
        public void onUpdate(StoreUpdateEvent<M> event) {
            for (M m : event.getItems()) {
                labelBuf.remove(event.getSource().getKeyProvider().getKey(m));
            }
        }

        @Override
        public void onDataChange(StoreDataChangeEvent<M> event) {
            labelBuf.clear();
        }

        @Override
        public void onRecordChange(StoreRecordChangeEvent<M> event) {
            labelBuf.remove(event.getSource().getKeyProvider().getKey(event.getRecord().getModel()));
        }

        @Override
        public void onSort(StoreSortEvent<M> event) {
        }
    }

    private class FilterChangeMonitor implements Scheduler.RepeatingCommand {

        private boolean runnig = true;
        private String lastUserStr = null;

        @Override
        public boolean execute() {
            if (runnig) {
                String s = field.getText();
                if (!Objects.equals(s, lastUserStr)) {
                    updateFilter();
                    this.lastUserStr = s;
                }
            }
            return runnig;
        }

        public void stop() {
            runnig = false;
        }
    }
}
