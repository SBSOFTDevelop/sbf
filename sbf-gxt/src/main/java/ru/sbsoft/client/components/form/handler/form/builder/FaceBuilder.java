package ru.sbsoft.client.components.form.handler.form.builder;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.Container;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.BrowserFactory;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.FormGridView2;
import ru.sbsoft.client.components.form.HasParentModelListener;
import ru.sbsoft.client.components.form.IFormFactory;
import ru.sbsoft.client.components.form.ModelChangeListener;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;
import ru.sbsoft.client.components.form.handler.form.LookupHandler;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.ContextGrid;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.GridUtils;
import ru.sbsoft.client.components.treebrowser.AbstractTreeGrid;
import ru.sbsoft.client.components.treebrowser.EditableTreeGrid;
import ru.sbsoft.client.components.treebrowser.FormTreeView;
import ru.sbsoft.sbf.app.form.IHasChangeValueHandlers;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.Modifier;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.filter.BigDecimalFilterInfo;
import ru.sbsoft.shared.filter.KeyFilterInfo;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public class FaceBuilder<DataModel extends IFormModel> implements IFaceBuilder<DataModel> {

    private final List<TabItem<DataModel>> tabItems = new ArrayList<>();
    private final TabPanel tabEditors;
    private final List<ModelChangeListener<DataModel>> modelChangeListeners = new ArrayList<>();
    private final List<ModelChangeListener<DataModel>> userModelChangeListeners = new ArrayList<>();
    private final String context;

    private String parentFilterName = null;

    private IParentIdProvider<DataModel> parentIdProvider = null;

    private final BaseForm<DataModel> form;

    public FaceBuilder(BaseForm<DataModel> form, TabPanel tabEditors, String context) {
        this.tabEditors = tabEditors;
        this.context = context;
        this.form = form;
    }
    
    public void setChildTabEnabled(IsWidget child, boolean enabled){
        ChildItem<?> ci = findChildItem(child);
        if(ci != null){
            ci.setTabEnabled(enabled);
        }
    }

    public void setParentIdProvider(IParentIdProvider<DataModel> parentIdProvider) {
        this.parentIdProvider = parentIdProvider;
    }

    @Override
    public void setParentFilterName(String parentFilterName) {
        this.parentFilterName = parentFilterName;
    }

    private String getParentFilterName() {
        if (parentFilterName == null && form.getOwnerGrid() != null) {
            parentFilterName = GridUtils.getContext(form.getOwnerGrid()).getGridType().getParentIdName();
        }
        return parentFilterName;
    }

    public List<IFormFieldHandler<? super DataModel>> getHandlers() {
        List<IFormFieldHandler<? super DataModel>> handlers = new ArrayList<>();
        tabEditors.clear();
        modelChangeListeners.clear();
        tabItems.forEach(t -> {
            handlers.addAll(t.getHandlers());
        });
        handlers.stream().filter(h -> (h instanceof LookupHandler)).map(h -> (LookupHandler) h).filter(lh -> (lh.isContextSupported() && lh.getContext() == null)).forEach(lh -> {
            lh.setContext(context);
        });
        tabItems.forEach(t -> {
            t.addSelf(tabEditors);
        });
        modelChangeListeners.addAll(userModelChangeListeners);
        return handlers;
    }

    @Override
    public void add(Tab<DataModel> tab) {
        tabItems.add(new SimpleItem(tab));
    }

    public List<ModelChangeListener<DataModel>> getModelChangeListeners() {
        return modelChangeListeners;
    }

    @Override
    public void addModelChangeListener(ModelChangeListener<DataModel> l) {
        if (l != null && !userModelChangeListeners.contains(l)) {
            userModelChangeListeners.add(l);
        }
    }

    @Override
    public <W extends IsWidget & ModelChangeListener<DataModel>> W add(String label, W w) {
        add(label, w, w);
        return w;
    }

    @Override
    public <W extends IsWidget> W add(String label, W w, ModelChangeListener<DataModel> l) {
        tabItems.add(new FreeItem(label, w, l));
        return w;
    }

    private GridMode[] mergeFlags(BrowserFactory g, GridMode... flags) {
        GridMode[] fflags = g.getBrowserFlags();
        if (fflags == null || fflags.length == 0) {
            return flags;
        }
        if (flags == null || flags.length == 0) {
            return fflags;
        }
        Set<GridMode> merge = EnumSet.copyOf(Arrays.asList(flags));
        merge.addAll(Arrays.asList(fflags));
        return merge.toArray(new GridMode[merge.size()]);
    }

    @Override
    public FormGridView2 addChildGrid(String label, BrowserFactory g, IFormFactory form, GridMode... flags) {
        if (form != null) {
            g.setForm(form);
        }
        return addChildGrid(label, g, flags);
    }

    @Override
    public FormGridView2 addChildGrid(String label, BrowserFactory g, GridMode... flags) {
        return addChildGrid(label, g.setContext(context).createGrid(), mergeFlags(g, flags));
    }

    @Override
    public FormGridView2 addChildGrid(String label, BaseGrid g, GridMode... flags) {
        GridItem item = new GridItem(label, g, flags);
        tabItems.add(item);
        return item.getView();
    }

    @Override
    public FormTreeView addChildTreeGrid(String label, AbstractTreeGrid g) {
        TreeGridItem item = new TreeGridItem(label, g);
        tabItems.add(item);
        if (g instanceof EditableTreeGrid) {
            IFormFactory f = ((EditableTreeGrid) g).getFormFactory();
            if (f instanceof HasParentModelListener) {
                addModelChangeListener(((HasParentModelListener) f).getParentModelListener());
            }
        }
        return item.getView();
    }

    @Override
    public FormTreeView addDeferredChildTreeGrid(String label, AbstractTreeGrid g, DataModel model) {
        TreeGridItem item = new TreeGridItem(label, g);
        // вызываем слушателей принудительно, так как лисенеры формы уже вызваны
        TreeGridFilterMaker m = new TreeGridFilterMaker(g);
        modelChangeListeners.add(m);
        m.modelChanged(model);
        if (g instanceof EditableTreeGrid) {
            IFormFactory f = ((EditableTreeGrid) g).getFormFactory();
            if (f instanceof HasParentModelListener) {
                ModelChangeListener listener = ((HasParentModelListener) f).getParentModelListener();
                modelChangeListeners.add(listener);
                listener.modelChanged(model);
            }
        }
        item.addSelf(tabEditors);
        return item.getView();
    }

    @Override
    public FormGridView2 addChildGrid(NamedGridType gt, GridMode... flags) {
        return addChildGrid(I18n.get(gt.getItemName()), gt, flags);
    }

    @Override
    public FormGridView2 addChildGrid(NamedGridType gt, IFormFactory form, GridMode... flags) {
        return addChildGrid(I18n.get(gt.getItemName()), gt, form, flags);
    }

    @Override
    public FormGridView2 addChildGrid(NamedGridType gt, IFormFactory form, Modifier mode, GridMode... flags) {
        return addChildGrid(I18n.get(gt.getItemName()), gt, form, mode, flags);
    }

    @Override
    public FormGridView2 addChildGrid(String label, NamedGridType gt, GridMode... flags) {
        return this.addChildGrid(label, gt, null, flags);
    }

    @Override
    public FormGridView2 addChildGrid(String label, NamedGridType gt, IFormFactory form, GridMode... flags) {
        return addChildGrid(label, gt, form, null, flags);
    }

    @Override
    public FormGridView2 addChildGrid(String label, NamedGridType gt, IFormFactory form, Modifier mode, GridMode... flags) {
        BrowserFactory br = new BrowserFactory(gt);
        if (form != null) {
            br.setForm(form);
        }
        if (mode != null) {
            br.addGridModifiers(mode);
        }
        return addChildGrid(label, br, flags);
    }

    public FaceBuilder<DataModel> delTab(String cap) {
        if (cap == null || cap.isEmpty()) {
            return this;
        }
        for (int i = 0; i < tabItems.size(); i++) {
            TabItem<DataModel> t = tabItems.get(i);
            if (cap.equalsIgnoreCase(t.getCap())) {
                tabItems.remove(i);
                break;
            }
        }
        return this;
    }

    @Override
    public <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addChildGridL(String label, BrowserFactory bf, F form, GridMode... flags) {
        if (form != null) {
            bf.setForm(form);
            addModelChangeListener(form.getParentModelListener());
        }
        return addChildGrid(label, bf, flags);
    }

    @Override
    public <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addChildGridL(String label, NamedGridType gt, F form, GridMode... flags) {
        return addChildGridL(label, gt, form, null, flags);
    }

    @Override
    public <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addChildGridL(String label, NamedGridType gt, F form, Modifier mode, GridMode... flags) {
        FormGridView2 res = addChildGrid(label, gt, form, mode, flags);
        addModelChangeListener(form.getParentModelListener());
        return res;
    }

    @Override
    public <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addDeferredChildGridL(String label, NamedGridType gt, F form, Modifier mode, DataModel model, GridMode... flags) {
        BrowserFactory br = new BrowserFactory(gt);
        if (form != null) {
            br.setForm(form);
        }
        if (mode != null) {
            br.addGridModifiers(mode);
        }
        ContextGrid grid = br.setContext(context).createGrid();
        GridItem item = new GridItem(label, grid, flags);
        item.addSelf(tabEditors);
        // вызываем слушателей принудительно, так как лисенеры формы уже вызваны
        GridFilterMaker m = new GridFilterMaker(grid);
        modelChangeListeners.add(m);
        m.modelChanged(model);
        if (form != null) {
            ModelChangeListener gridListener = form.getParentModelListener();
            modelChangeListeners.add(gridListener);
            gridListener.modelChanged(model);
        }
        return item.getView();
    }

    @Override
    public FormGridView2 addChildGridAfter(String afterTabLabel, String label, BaseGrid g, GridMode... flags) {
        GridItem item = new GridItem(label, g, flags);
        addAfter(afterTabLabel, item);
        return item.getView();
    }

    @Override
    public FormGridView2 addChildGridAfter(String afterTabLabel, String label, NamedGridType gt, GridMode... flags) {
        return this.addChildGridAfter(afterTabLabel, label, gt, null, flags);
    }

    @Override
    public FormGridView2 addChildGridAfter(String afterTabLabel, String label, NamedGridType gt, IFormFactory form, GridMode... flags) {
        BrowserFactory br = new BrowserFactory(gt);
        if (form != null) {
            br.setForm(form);
        }
        return addChildGridAfter(afterTabLabel, label, br.createGrid(), flags);
    }

    @Override
    public void addAfter(String cap, Tab<DataModel> tab) {
        this.addAfter(cap, new SimpleItem(tab));
    }

    public void addAfter(String cap, TabBundle<DataModel> tabs) {
        if (tabs != null && tabs.getTabs() != null) {
            List<Tab<DataModel>> tbs = tabs.getTabs();
            TabItem[] ta = new TabItem[tbs.size()];
            for (int i = 0; i < tbs.size(); i++) {
                ta[i] = new SimpleItem(tbs.get(i));
            }
            addAfter(cap, ta);
        }
    }

    private FaceBuilder<DataModel> addAfter(String cap, TabItem<DataModel>... items) {
        if (cap == null || cap.isEmpty()) {
            tabItems.addAll(0, Arrays.asList(items));
        } else {
            for (int i = 0; i < tabItems.size(); i++) {
                TabItem<DataModel> t = tabItems.get(i);
                if (cap.equalsIgnoreCase(t.getCap())) {
                    tabItems.addAll(i + 1, Arrays.asList(items));
                    break;
                }
            }
        }
        return this;
    }

    @Override
    public Tab<DataModel> getTab(String cap) {
        if (cap == null || cap.isEmpty()) {
            return null;
        }
        for (TabItem<DataModel> t : tabItems) {
            if (cap.equalsIgnoreCase(t.getCap())) {
                return t.getTab();
            }
        }
        return null;
    }

    @Override
    public Tab<DataModel> getTab(int index) {
        return tabItems != null && tabItems.size() > index ? tabItems.get(index).getTab() : null;
    }

    @Override
    public Tab<DataModel> getMainTab() {
        return getTab(0);
    }

    private class GridFilterMaker implements ModelChangeListener<DataModel> {

        private final BaseGrid g;

        public GridFilterMaker(BaseGrid g) {
            this.g = g;
        }

        @Override
        public void modelChanged(DataModel m) {
            BigDecimal id = m != null ? parentIdProvider != null ? parentIdProvider.getParentId(m) : m.getId() : null;
            g.setParentFilters(FilterInfo.list(new BigDecimalFilterInfo(getParentFilterName(), id != null ? id : KeyFilterInfo.FILTER_NOTEXIST_VALUE)));
        }

    }

    private class TreeGridFilterMaker implements ModelChangeListener<DataModel> {

        private final AbstractTreeGrid g;

        public TreeGridFilterMaker(AbstractTreeGrid g) {
            this.g = g;
        }

        @Override
        public void modelChanged(DataModel m) {
            BigDecimal id = m != null ? parentIdProvider != null ? parentIdProvider.getParentId(m) : m.getId() : null;
            g.setParentFilters(FilterInfo.list(new BigDecimalFilterInfo(getParentFilterName(), id != null ? id : KeyFilterInfo.FILTER_NOTEXIST_VALUE)));
        }

    }

    private void disableOnNew(TabPanel tabEditors, Widget w) {
        modelChangeListeners.add(new ChildTabDisabler(tabEditors, w));
    }

    private interface TabItem<DataModel extends IFormModel> {

        String getCap();

        void addSelf(TabPanel tabEditors);

        List<IFormFieldHandler<? super DataModel>> getHandlers();

        Tab<DataModel> getTab();
    }

    private class SimpleItem implements TabItem<DataModel> {

        private final Tab<DataModel> tab;

        public SimpleItem(Tab<DataModel> tab) {
            this.tab = tab;
        }

        @Override
        public Tab<DataModel> getTab() {
            return tab;
        }

        @Override
        public String getCap() {
            return tab.getCaption();
        }

        @Override
        public void addSelf(TabPanel tabEditors) {
            Container view = tab.getContainer();
            tabEditors.add(view, new TabItemConfig(tab.getCaption(), false));
            if (tab.isDisableOnNew()) {
                disableOnNew(tabEditors, view);
            }
        }

        @Override
        public List<IFormFieldHandler<? super DataModel>> getHandlers() {
            return tab.getHandlers();
        }

    }
    
    private ChildItem<?> findChildItem(IsWidget w){
        Widget ww = w.asWidget();
        for(TabItem<DataModel> ti : tabItems){
            if((ti instanceof ChildItem) && ((ChildItem<?>)ti).view == ww){
                return (ChildItem<?>)ti;
            }
        }
        return null;
    }

    private interface ChildItemControl {

        void setTabEnabled(boolean enabled);
    }

    private abstract class ChildItem<V extends Widget> implements TabItem<DataModel>, ChildItemControl {

        protected final String label;
        protected final V view;
        private boolean tabEnabled = true;

        public ChildItem(String label, V w) {
            this.label = label;
            this.view = w;
        }

        @Override
        public String getCap() {
            return label;
        }

        @Override
        public void addSelf(TabPanel tabEditors) {
            disableOnNew(tabEditors, view);
            TabItemConfig cfg = new TabItemConfig(label, false);
            cfg.setEnabled(tabEnabled);
            tabEditors.add(view, cfg);
        }

        @Override
        public void setTabEnabled(boolean enabled) {
            if (enabled ^ tabEnabled) {
                tabEnabled = enabled;
                TabItemConfig cfg = tabEditors != null ? tabEditors.getConfig(view) : null;
                if (cfg != null) {
                    cfg.setEnabled(enabled);
                    tabEditors.update(view, cfg);
                }
            }
        }

        @Override
        public List<IFormFieldHandler<? super DataModel>> getHandlers() {
            return Collections.emptyList();
        }

        @Override
        public Tab<DataModel> getTab() {
            throw new ApplicationException("Free tab is not in Tab envelope");
        }
    }

    private class FreeItem extends ChildItem<Widget> {

        private final ModelChangeListener<DataModel> changeListener;
        private final IsWidget w;

        public FreeItem(String label, IsWidget wigdet) {
            this(label, wigdet, null);
        }

        public FreeItem(String label, IsWidget wigdet, ModelChangeListener<DataModel> l) {
            super(label, wigdet.asWidget());
            this.changeListener = l;
            this.w = wigdet;
        }

        @Override
        public void addSelf(TabPanel tabEditors) {
            super.addSelf(tabEditors);
            if (changeListener != null) {
                modelChangeListeners.add(changeListener);
            }
            if (w instanceof IHasChangeValueHandlers) {
                ((IHasChangeValueHandlers) w).addChangeValueHandler((v) -> form.setChanged(true));
            }
        }
    }

    private class GridItem extends ChildItem<FormGridView2> {

        private final BaseGrid g;

        public GridItem(String label, BaseGrid g, GridMode[] flags) {
            super(label, new FormGridView2(label, g, false, flags));
            this.g = g;
        }

        public FormGridView2 getView() {
            return view;
        }

        @Override
        public void addSelf(TabPanel tabEditors) {
            super.addSelf(tabEditors);
            modelChangeListeners.add(new GridFilterMaker(g));
        }
    }

    private class TreeGridItem extends ChildItem<FormTreeView> {

        private final AbstractTreeGrid g;

        public TreeGridItem(String label, AbstractTreeGrid g) {
            super(label, new FormTreeView(label, g));
            this.g = g;
        }

        public FormTreeView getView() {
            return view;
        }

        @Override
        public void addSelf(TabPanel tabEditors) {
            super.addSelf(tabEditors);
            modelChangeListeners.add(new TreeGridFilterMaker(g));
        }

    }

    private class ChildTabDisabler implements ModelChangeListener<DataModel> {

        private final TabPanel tabEditors;
        private final Widget view;

        public ChildTabDisabler(TabPanel tabEditors, Widget view) {
            this.tabEditors = tabEditors;
            this.view = view;
        }

        @Override
        public void modelChanged(DataModel m) {
            TabItemConfig conf = tabEditors.getConfig(view);
            if (conf.isEnabled() ^ (m != null && m.getId() != null)) {
                conf.setEnabled(m.getId() != null);
                tabEditors.update(view, conf);
            }
        }
    }
}
