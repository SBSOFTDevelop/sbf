package ru.sbsoft.client.components.form.handler.form.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.components.form.SimplePageFormContainer;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public class Tab<DataModel extends IFormModel> {

    protected static final int TAB_LABEL_WIDTH = 120;
    private SimplePageFormContainer containerBase;
    private final String caption;
    private final List<FSet<DataModel>> fsets = new ArrayList<>();
    private int labelWidth;
    private boolean disableOnNew = false;

    public Tab(String cap) {
        this(cap, TAB_LABEL_WIDTH);
    }

    public Tab(String cap, int labelWidth) {
        this.caption = cap;
        this.labelWidth = labelWidth;
    }

    public Tab setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
        return this;
    }

    SimplePageFormContainer getContainer() {
        return containerBase;
    }

    String getCaption() {
        return caption;
    }

    public Tab setDisableOnNew(boolean disableOnNew) {
        this.disableOnNew = disableOnNew;
        return this;
    }

    public boolean isDisableOnNew() {
        return disableOnNew;
    }

    List<IFormFieldHandler<? super DataModel>> getHandlers() {
        List<IFormFieldHandler<? super DataModel>> hh = new ArrayList<>();
        for (FSet<DataModel> s : fsets) {
            hh.addAll(s.getHandlers());
        }
        containerBase = new SimplePageFormContainer(labelWidth);
        for (FSet<DataModel> fset : fsets) {
            containerBase.addFieldSet(fset.getManagedSet(), fset.isResizable());
            if (fset.getManagedSet().isCollapsible()) {

                fset.addCollapseHandler((event) -> {

                    containerBase.forceLayout();
                    fset.getManagedSet().forceLayout();
                });

                fset.addExpandHandler((event) -> {

                    fset.getManagedSet().forceLayout();
                    containerBase.forceLayout();
                });

            }

        }
        return hh;
    }

    public Tab<DataModel> add(FSet<DataModel>... sets) {
        if (sets != null && sets.length > 0) {
            for (FSet<DataModel> s : sets) {
                if (s != null) {
                    fsets.add(s);
                }
            }
        }
        return this;
    }

    public Tab<DataModel> add(FSetBundle<DataModel> b) {
        if (b != null) {
            List<FSet<DataModel>> sets = b.getSets();
            if (sets != null) {
                fsets.addAll(sets);
            }
        }
        return this;
    }

    public Tab<DataModel> delSet(String cap) {
        if (cap == null || cap.isEmpty()) {
            return this;
        }
        for (int i = 0; i < fsets.size(); i++) {
            FSet<DataModel> f = fsets.get(i);
            if (cap.equalsIgnoreCase(f.getCap())) {
                fsets.remove(i);
                break;
            }
        }
        return this;
    }

    public Tab<DataModel> addAfter(String cap, FSetBundle<DataModel> b) {
        addAfterInternal(cap, b != null && b.getSets() != null ? b.getSets() : Collections.<FSet<DataModel>>emptyList());
        return this;
    }

    public Tab<DataModel> addAfter(String cap, FSet<DataModel>... sets) {
        addAfterInternal(cap, Arrays.asList(sets));
        return this;
    }

    public Tab<DataModel> addAfter(FSet<DataModel> set, FSetBundle<DataModel> b) {
        addAfterInternal(set, b != null && b.getSets() != null ? b.getSets() : Collections.<FSet<DataModel>>emptyList());
        return this;
    }

    public Tab<DataModel> addFirst(FSet<DataModel>... sets) {
        return this.addAfter((FSet<DataModel>) null, sets);
    }

    public Tab<DataModel> addAfter(FSet<DataModel> set, FSet<DataModel>... sets) {
        addAfterInternal(set, Arrays.asList(sets));
        return this;
    }

    private Tab<DataModel> addAfterInternal(String cap, List<FSet<DataModel>> sets) {
        FSet<DataModel> set;
        if (cap == null) {
            set = null;
        } else {
            set = getSet(cap);
            if (set == null) {
                throw new IllegalArgumentException("FSet with CAP '" + cap + "' is not found");
            }
        }
        return addAfterInternal(set, sets);
    }

    private Tab<DataModel> addAfterInternal(FSet<DataModel> set, List<FSet<DataModel>> sets) {
        if (set == null) {
            fsets.addAll(0, sets);
        } else {
            for (int i = 0; i < fsets.size(); i++) {
                FSet<DataModel> f = fsets.get(i);
                if (set == f) {
                    fsets.addAll(i + 1, sets);
                    break;
                }
            }
        }
        return this;
    }

    public FSet<DataModel> getSet(String cap) {
        if (cap == null || cap.trim().isEmpty()) {
            return null;
        }
        for (FSet<DataModel> f : fsets) {
            if (f != null && cap.equalsIgnoreCase(f.getCap())) {
                return f;
            }
        }
        return null;
    }
}
