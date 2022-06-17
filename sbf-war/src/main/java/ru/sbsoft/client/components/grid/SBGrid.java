package ru.sbsoft.client.components.grid;

import ru.sbsoft.svc.data.shared.loader.ListLoader;
import ru.sbsoft.svc.widget.core.client.grid.ColumnHeader;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.GridSelectionModel;
import ru.sbsoft.svc.widget.core.client.grid.GridView;
import ru.sbsoft.svc.widget.core.client.grid.HeaderGroupConfig;
import ru.sbsoft.svc.widget.core.client.tips.QuickTip;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import static ru.sbsoft.client.components.grid.GridConsts.MARK_PROP;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.services.FetchParams;
import ru.sbsoft.shared.services.FetchResult;

/**
 *
 * @author Kiselev
 * @param <M> класс данныых для строки таблицы
 */
public class SBGrid<M extends MarkModel> extends Grid<M> implements QuickTipAllowed {

    private QuickTip quickTip = null;
    private Deque<Boolean> loadMaskFlagStack = new ArrayDeque<>(3);

    private List<GridSelectionModelChangeListener<M>> gridSelectionModelChangeListeners = new ArrayList<>();

    public SBGrid(SBLiveGridView<M> view, ListLoader<FetchParams, FetchResult<MarkModel>> loader) {
        this(null, view, loader);
    }

    public SBGrid(ColumnModel<M> cm, SBLiveGridView<M> view, ListLoader<FetchParams, FetchResult<MarkModel>> loader) {
        super(new SBListStore<M>(MARK_PROP.key()), cm != null ? cm : new ColumnModel(Collections.emptyList()), view);
        view.setColumnHeader(new SBColumnHeader<>(this, cm));
        setColumnReordering(true);
        setLoadMask(true);
        super.setLoader(loader);
    }

    
    public void addGridSelectionModelChangeListener(GridSelectionModelChangeListener<M> l) {
        if (!gridSelectionModelChangeListeners.contains(l)) {
            gridSelectionModelChangeListeners.add(l);
        }
    }

    private void fireModelChanged() {
        if (gridSelectionModelChangeListeners != null) {
            GridSelectionModel<M> m = getSelectionModel();
            for (GridSelectionModelChangeListener<M> l : gridSelectionModelChangeListeners) {
                l.onGridSelectionModelChanged(m);
            }
        }
    }

    public void pushLoadMask(boolean newFlag) {
        loadMaskFlagStack.push(isLoadMask());
        super.setLoadMask(newFlag);
    }

    public void popLoadMask() {
        Boolean v = loadMaskFlagStack.poll();
        if (v != null) {
            super.setLoadMask(v);
        }
    }

    @Override
    public void setQuickTip(boolean on) {
        if (quickTip == null && on) {
            quickTip = new QuickTip(this);
        } else if (quickTip != null && !on) {
            quickTip.initTarget(null);
            quickTip = null;
        }
    }

    @Override
    public void setSelectionModel(GridSelectionModel<M> sm) {
        super.setSelectionModel(sm);
        fireModelChanged();
    }

    @Override
    public void setView(GridView<M> view) {
        throw new UnsupportedOperationException("View replace is not supported");
    }

    @Override
    public SBLiveGridView<M> getView() {
        return (SBLiveGridView<M>) super.getView();
    }

    @Override
    public void setLoader(ListLoader<?, ?> loader) {
        throw new UnsupportedOperationException("Loader replace is not supported");
    }

    private static class SBColumnHeader<M> extends ColumnHeader<M> {

        public SBColumnHeader(Grid<M> container, ColumnModel<M> cm) {
            super(container, cm);
        }

        @Override
        protected Group createNewGroup(HeaderGroupConfig config) {
            return new SBGroup(config);
        }

        private class SBGroup extends Group {

            public SBGroup(HeaderGroupConfig config) {
                super(config);
                if (config.getRowspan() > 1) {
                    getElement().getStyle().setProperty("whiteSpace", "normal");
                }
            }
        }
    }
}
