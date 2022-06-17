package ru.sbsoft.client.components.form;

import java.util.List;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.browser.BrowserConfigHelper;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.CustomGridToolBar;
import ru.sbsoft.client.components.grid.GridMode;
import ru.sbsoft.client.components.grid.GridToolBar;
import ru.sbsoft.client.components.operation.GridOperationMaker;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.FileFormat;
import ru.sbsoft.client.components.operation.TypedGridOperationAction;

/**
 *
 * @author Kiselev
 * @param <GR>
 */
public class FormGridView2<GR extends BaseGrid> extends FormGridView {
    
    private final boolean compactView;
    private final boolean noMenu;
    private final BrowserConfigHelper h;
    
    public FormGridView2(GR grid, GridMode... flags) {
        this(null, grid, flags);
    }
    
    public FormGridView2(String header, GR grid, GridMode... flags) {
        this(header, grid, false, flags);
    }
    
    public FormGridView2(final GR grid, boolean compactView, GridMode... flags) {
        this(null, grid, compactView, flags);
    }
    
    public FormGridView2(String header, final GR grid, boolean compactView, GridMode... flags) {
        this(header, grid, compactView, false, flags);
    }
    
    public FormGridView2(String header, GR grid, boolean compactView, boolean noMenu, GridMode... flags) {
        super(grid, compactView, noMenu, flags);
        this.compactView = compactView;
        this.noMenu = noMenu;
        if (header != null) {
            super.setHeadingText(header);
        }
        h = new BrowserConfigHelper(this);
    }
    
    @Override
    public GR getGrid() {
        return (GR) super.getGrid();
    }
    
    @Override
    protected CustomGridToolBar createGridToolBar() {
        return compactView ? super.createGridToolBar() : new GridToolBar(this, true);
    }
    
    public boolean isCompactView() {
        return compactView;
    }
    
    public boolean isNoMenu() {
        return noMenu;
    }
    
    public FormGridView2 addReport(GridOperationMaker maker, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        h.addReport(maker, useMode);
        return this;
    }
    
    public FormGridView2 addReport(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        h.addReport(type, paramsFormFactory, useMode);
        return this;
    }
    
    public FormGridView2 addReport(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode, FileFormat... formats) {
        h.addReport(type, useMode, formats);
        return this;
    }
    
    public FormGridView2 addOperation(GridOperationMaker maker, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        h.addOperation(maker, useMode);
        return this;
    }
    
    public FormGridView2 addOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        h.addOperation(type, paramsFormFactory, useMode);
        return this;
    }
    
    public FormGridView2 addOperation(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        h.addOperation(type, useMode);
        return this;
    }
    
    public void addAction(Action a) {
        h.addAction(a);
    }
    
    public void addActionsButton(Action... a) {
        h.addActionsButton(a);
    }

    public void addActionsButton(List<Action> a) {
        h.addActionsButton(a.toArray(new Action[0]));
    }
    
    
    
    public void addActionsButton(int beforeIndex, Action... a) {
        h.addActionsButton(beforeIndex, a);
        
    }
    
    
    
}
