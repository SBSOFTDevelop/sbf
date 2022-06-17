package ru.sbsoft.client.components.form.handler.form.builder;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.Container;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.BeforeCollapseEvent;
import ru.sbsoft.svc.widget.core.client.event.BeforeExpandEvent;
import ru.sbsoft.svc.widget.core.client.event.CollapseEvent;
import ru.sbsoft.svc.widget.core.client.event.ExpandEvent;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ru.sbsoft.client.components.form.TwoFieldHorizontalContainer;
import ru.sbsoft.client.components.form.handler.BaseHandler;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.sbf.svc.components.FieldsContainer;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.sbf.svc.utils.FieldUtils;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public class FSet<DataModel extends IFormModel> {

    private String cap;
    private final ItemStore<DataModel> itemStore = new ItemStore();
    private final VerticalFieldSet fset;

    private boolean resizable;

    public FSet(String cap) {
        this.cap = cap;
        fset = new MyVericalFieldSet(cap);

    }

    public FSet<DataModel> setResizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public FSet<DataModel> setCollapsible(boolean collapsible) {
        setCollapsible(collapsible, true);
        return this;
    }

    public boolean isResizable() {
        return resizable;
    }
    
    public boolean hasFields(){
        return !itemStore.getItems().isEmpty();
    }

    public HandlerRegistration addBeforeCollapseHandler(BeforeCollapseEvent.BeforeCollapseHandler handler) {
        return fset.addBeforeCollapseHandler(handler);
    }

    public HandlerRegistration addBeforeExpandHandler(BeforeExpandEvent.BeforeExpandHandler handler) {
        return fset.addBeforeExpandHandler(handler);
    }

    public HandlerRegistration addCollapseHandler(CollapseEvent.CollapseHandler handler) {
        return fset.addCollapseHandler(handler);
    }

    public HandlerRegistration addExpandHandler(ExpandEvent.ExpandHandler handler) {
        return fset.addExpandHandler(handler);
    }

    public FSet<DataModel> setCollapsible(boolean collapsible, boolean isExpand) {
        fset.setCollapsible(collapsible);
        fset.setExpanded(isExpand);

        return this;
    }

    protected VerticalFieldSet getManagedSet() {
        return fset;
    }

    public String getCap() {
        return cap;
    }

    public FSet<DataModel> setCap(String cap) {
        this.cap = cap;
        fset.setHeading(cap);
        return this;
    }

    public FSet<DataModel> setEnabled(boolean b) {
        fset.setEnabled(b);
        return this;
    }

    public FSet<DataModel> setVisible(boolean b) {
        fset.setVisible(b);
        if (b) {
            fset.forceLayout();
            if (fset.getParent() instanceof VerticalLayoutContainer && fset.isAttached()) {
                ((VerticalLayoutContainer) fset.getParent()).forceLayout();

            }

        }
        return this;
    }

    public void setChildrenReq(boolean b) {
        itemStore.getItems().forEach(it -> {
            it.setReq(b);
        });
    }

    protected List<IFormFieldHandler<? super DataModel>> doBuild(List<IFormFieldHandler<? super DataModel>> handlers) {
        fset.clear();
        handlers.clear();
        itemStore.getItems().forEach(item -> {
            item.add(fset, handlers);
        });
        return handlers;
    }

    List<IFormFieldHandler<? super DataModel>> getHandlers() {
        return doBuild(new ArrayList<>());
    }

    public <F extends Component> FSet<DataModel> addField(String label, F f) {
        if (f != null) {
            itemStore.add(new FieldItem(label, f));
        }
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> add(H... fields) {
        return add(fields != null && fields.length > 0 ? Arrays.asList(fields) : null);
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> add(List<H> fields) {
        if (fields != null && fields.size() > 0) {
            for (H field : fields) {
                if (field != null) {
                    itemStore.add(new SimpleItem(field));
                }
            }
        }
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addComposite(String label, Double part, H f1, H f2) {
        itemStore.add(new ComposItem(label, f1, f2, part));
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addComposite(String label, H f1, H f2) {
        itemStore.add(new ComposItem(label, f1, f2));
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> add(DualField<DataModel, H> f) {
        if (f != null) {
            addComposite(f.getLabel(), f.getF1(), f.getF2());
        }
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addIf(boolean condition, H... fields) {
        if (condition) {
            add(fields);
        }
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addTextBundle(String label, int fieldWidth, H... fields) {
        itemStore.add(new BundleItem(label, fieldWidth, fields));
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addTextBundle(String label, TextButton btn, int fieldWidth, H... fields) {
        itemStore.add(new BundleItem(label, btn, fieldWidth, fields));
        return this;
    }

    public FSet<DataModel> setDefaultReq(Boolean req) {
        itemStore.setReq(req);
        return this;
    }

    public FSet<DataModel> del(String label) {
        itemStore.del(label);
        return this;
    }

    public <F extends Component> FSet<DataModel> addFieldAfter(String afterLabel, String label, F field) {
        itemStore.add(true, afterLabel, new FieldItem(label, field));
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addFirst(H... fields) {
        return addAfter((String) null, fields);
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addAfter(String label, H... fields) {
        return addAfter(label, fields != null && fields.length > 0 ? Arrays.asList(fields) : null);
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addAfter(String label, List<H> fields) {
        itemStore.add(true, label, toItems(fields));
        return this;
    }

    public <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> FSet<DataModel> addAfter(String label, DualField<DataModel, H> f) {
        itemStore.add(true, label, new ComposItem(f.getLabel(), f.getF1(), f.getF2(), f.getPart()));
        return this;
    }

    private <H extends IFieldHandler & IFormFieldHandler<? super DataModel>> Item<DataModel>[] toItems(List<H> fields) {
        List<Item<? super DataModel>> it = new ArrayList<>();
        for (H h : fields) {
            if (h != null) {
                it.add(new SimpleItem<>(h));
            }
        }
        return it.toArray(new Item[it.size()]);
    }

    private static class ItemStore<DataModel extends IFormModel> {

        private final List<Item<DataModel>> items = new ArrayList<>();
        private Boolean req = null;

        public void add(Item<DataModel>... hhh) {
            for (Item<DataModel> hh : hhh) {
                items.add(hh);
                if (req != null) {
                    hh.setReq(req);
                }
            }
        }

        public void setReq(Boolean req) {
            this.req = req;
        }

        public List<Item<DataModel>> getItems() {
            return items;
        }

        public void add(boolean after, String label, Item<DataModel>... hhh) {
            if (label == null || label.isEmpty()) {
                items.addAll(0, Arrays.asList(hhh));
            } else {
                for (int i = 0; i < items.size(); i++) {
                    Item<DataModel> item = items.get(i);
                    if (label.equalsIgnoreCase(item.getLabel())) {
                        items.addAll(after ? i + 1 : i, Arrays.asList(hhh));
                        break;
                    }
                }
            }
        }

        public void del(String label) {
            if (label == null || label.isEmpty()) {
                return;
            }
            for (int i = 0; i < items.size(); i++) {
                Item<DataModel> item = items.get(i);
                if (label.equalsIgnoreCase(item.getLabel())) {
                    items.remove(i);
                    break;
                }
            }
        }

    }

    private interface Item<DataModel extends IFormModel> {

        void setReq(boolean b);

        String getLabel();

        void add(VerticalFieldSet fset, List<IFormFieldHandler<? super DataModel>> handlers);
    }

    private static class FieldItem<F extends Component> implements Item<IFormModel> {

        private final String label;
        private final F field;
        private final FieldHandleHelper helper;

        public FieldItem(String label, F field) {
            this.label = label;
            this.field = field;
            this.helper = new FieldHandleHelper(label);
        }

        @Override
        public void setReq(boolean b) {
            helper.setReq(b);
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void add(VerticalFieldSet fset, List<IFormFieldHandler<? super IFormModel>> handlers) {
            fset.addField(helper.getWidget());
        }

        private class FieldHandleHelper extends BaseHandler<F, Object, FieldHandleHelper> {

            public FieldHandleHelper(String label) {
                super(null, label);
            }

            @Override
            protected F createField() {
                return field;
            }

            @Override
            protected FilterInfo createFilter() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected ParamInfo createParamInfo() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }
    }

    private static class SimpleItem<DataModel extends IFormModel, H extends IFieldHandler & IFormFieldHandler<? super DataModel>> implements Item<DataModel> {

        private final H h;

        public SimpleItem(H h) {
            this.h = h;
        }

        @Override
        public String getLabel() {
            return h.getLabel();
        }

        @Override
        public void add(VerticalFieldSet fset, List<IFormFieldHandler<? super DataModel>> handlers) {
            fset.addField(h.getWidget());
            handlers.add(h);
        }

        @Override
        public void setReq(boolean b) {
            h.setReq(b);
        }
    }

    private static class ComposItem<DataModel extends IFormModel, H extends IFieldHandler & IFormFieldHandler<? super DataModel>> implements Item<DataModel> {

        private final String label;
        private final H f1;
        private final H f2;
        private final Double part;

        public ComposItem(String label, H f1, H f2) {
            this(label, f1, f2, null);
        }

        public ComposItem(String label, H f1, H f2, Double part) {
            this.label = label;
            this.f1 = f1;
            this.f2 = f2;
            this.part = part;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void add(VerticalFieldSet fset, List<IFormFieldHandler<? super DataModel>> handlers) {
            fset.addField(new FieldLabel(new TwoFieldHorizontalContainer(f1.getField(), f2.getField(), part != null ? part : -1), label));
            handlers.add(f1);
            handlers.add(f2);
        }

        @Override
        public void setReq(boolean b) {
            f1.setReq(b);
        }

    }

    private static class BundleItem<DataModel extends IFormModel, H extends IFieldHandler & IFormFieldHandler<? super DataModel>> implements Item<DataModel> {

        private final String label;
        private final int fieldWidth;
        private final H[] fields;
        private final TextButton btn;

        public BundleItem(String label, int fieldWidth, H... fields) {
            this(label, null, fieldWidth, fields);
        }

        public BundleItem(String label, TextButton btn, int fieldWidth, H... fields) {
            this.label = label;
            this.fieldWidth = fieldWidth;
            this.fields = fields;
            this.btn = btn;
        }

        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void add(VerticalFieldSet fset, List<IFormFieldHandler<? super DataModel>> handlers) {
            FieldsContainer container = new FieldsContainer();
            if (fields != null && fields.length > 0) {
                for (int i = 0; i < fields.length - 1; i++) {
                    H f = fields[i];
                    if (fieldWidth > 0) {
                        f.getField().setWidth(fieldWidth);
                    }
                    container.add(f.getField(), HLC.CONST);
                    container.add(FieldUtils.createSeparator(), HLC.CONST);
                }
                container.add(fields[fields.length - 1].getField(), HLC.FILL);
            }
            if (btn != null) {
                if (fields != null && fields.length > 0) {
                    container.add(FieldUtils.createSeparator(), HLC.CONST);
                }
                container.add(btn, HLC.CONST);
            }
            fset.addField(new FieldLabel(container, label));
            handlers.addAll(Arrays.asList(fields));
        }

        @Override
        public void setReq(boolean b) {
            for (H f : fields) {
                f.setReq(b);
            }
        }

    }

    private static class MyVericalFieldSet extends VerticalFieldSet {

        public MyVericalFieldSet(String caption) {
            super(caption);
        }

        @Override
        public void clear() {
            ((Container) getChildren().get(0)).clear();
        }

    }
}
