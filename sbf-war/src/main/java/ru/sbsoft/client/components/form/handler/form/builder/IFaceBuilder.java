package ru.sbsoft.client.components.form.handler.form.builder;

import com.google.gwt.user.client.ui.IsWidget;
import ru.sbsoft.client.components.browser.BrowserFactory;
import ru.sbsoft.client.components.form.FormGridView2;
import ru.sbsoft.client.components.form.HasParentModelListener;
import ru.sbsoft.client.components.form.IFormFactory;
import ru.sbsoft.client.components.form.ModelChangeListener;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.treebrowser.AbstractTreeGrid;
import ru.sbsoft.client.components.treebrowser.FormTreeView;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.Modifier;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public interface IFaceBuilder<DataModel extends IFormModel> {

    void setChildTabEnabled(IsWidget child, boolean enabled);

    void add(Tab<DataModel> tab);

    <W extends IsWidget & ModelChangeListener<DataModel>> W add(String label, W w);

    <W extends IsWidget> W add(String label, W w, ModelChangeListener<DataModel> l);

    void setParentFilterName(String childFilterName);

    FormGridView2 addChildGrid(String label, BaseGrid g, GridMode... flags);

    FormGridView2 addChildGrid(String label, BrowserFactory bf, GridMode... flags);

    FormGridView2 addChildGrid(String label, BrowserFactory bf, IFormFactory form, GridMode... flags);

    FormGridView2 addChildGrid(String label, NamedGridType gt, GridMode... flags);

    FormGridView2 addChildGrid(String label, NamedGridType gt, IFormFactory form, GridMode... flags);

    FormGridView2 addChildGrid(String label, NamedGridType gt, IFormFactory form, Modifier mode, GridMode... flags);

    FormGridView2 addChildGrid(NamedGridType gt, GridMode... flags);

    FormGridView2 addChildGrid(NamedGridType gt, IFormFactory form, GridMode... flags);

    FormGridView2 addChildGrid(NamedGridType gt, IFormFactory form, Modifier mode, GridMode... flags);

    FormTreeView addChildTreeGrid(String label, AbstractTreeGrid g);

    FormTreeView addDeferredChildTreeGrid(String label, AbstractTreeGrid g, DataModel model);

    <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addChildGridL(String label, BrowserFactory bf, F form, GridMode... flags);

    <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addChildGridL(String label, NamedGridType gt, F form, GridMode... flags);

    <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addChildGridL(String label, NamedGridType gt, F form, Modifier mode, GridMode... flags);

    <F extends IFormFactory & HasParentModelListener<DataModel>> FormGridView2 addDeferredChildGridL(String label, NamedGridType gt, F form, Modifier mode, DataModel model, GridMode... flags);

    FormGridView2 addChildGridAfter(String afterTabLabel, String label, BaseGrid g, GridMode... flags);

    FormGridView2 addChildGridAfter(String afterTabLabel, String label, NamedGridType gt, GridMode... flags);

    FormGridView2 addChildGridAfter(String afterTabLabel, String label, NamedGridType gt, IFormFactory form, GridMode... flags);

    void addAfter(String cap, Tab<DataModel> tab);

    Tab<DataModel> getTab(String cap);

    Tab<DataModel> getTab(int index);

    Tab<DataModel> getMainTab();

    void addModelChangeListener(ModelChangeListener<DataModel> l);

}
