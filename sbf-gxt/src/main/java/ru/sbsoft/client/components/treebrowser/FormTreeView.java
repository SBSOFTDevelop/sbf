package ru.sbsoft.client.components.treebrowser;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.VLC;

/**
 *
 * @author sokolov
 */
public class FormTreeView extends TreeBrowser implements ReadOnlyControl {

    private final ContentPanel contentPanel = new ContentPanel();
    private final AbstractTreeGrid treeGrid;

    public FormTreeView(String header, AbstractTreeGrid treeGrid) {
        super(true);
        this.treeGrid = treeGrid;
        if (null == header) {
            this.contentPanel.setHeaderVisible(false);
        } else {
            this.contentPanel.setHeading(header);
        }
    }

    @Override
    protected void init() {
        grid = createGrid();
        grid.setActionManager(actionManager);
        VerticalLayoutContainer vContainer = new VerticalLayoutContainer();
        vContainer.add(getGridMenu());
        vContainer.add(getGridToolBar(), VLC.CONST);
        vContainer.add(grid, VLC.FILL);

        contentPanel.getHeader().setIcon(SBFResources.APP_ICONS.Table());
        contentPanel.setWidget(vContainer);

        add(contentPanel, VLC.FILL);
        grid.refresh(true);
        
    }

    @Override
    public boolean isReadOnly() {
        return grid.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        grid.setReadOnly(readOnly);
    }

    @Override
    protected AbstractTreeGrid createGrid() {
        return treeGrid;
    }
    
    @Override
    public TreeBrowserCloseAction getCloseAction() {
        return null;
    }

}
