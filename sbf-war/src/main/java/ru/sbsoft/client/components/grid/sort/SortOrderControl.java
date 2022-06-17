package ru.sbsoft.client.components.grid.sort;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.cell.core.client.ButtonCell;
import ru.sbsoft.svc.data.shared.SortDir;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.grid.SvcGridResource;

/**
 *
 * @author Kiselev
 */
public class SortOrderControl extends TextButton {

    private static final ImageResource ICO_ASC = SvcGridResource.getSortAscendingIcon();
    private static final ImageResource ICO_DESC = SvcGridResource.getSortDescendingIcon();

    private SortDir dir = SortDir.ASC;

    public SortOrderControl() {
        setScale(ButtonCell.ButtonScale.SMALL);
        setIconAlign(ButtonCell.IconAlign.LEFT);
        setIcon(ICO_ASC);
    }

    public void setDir(SortDir d) {
        if (d != dir) {
            dir = d;
            setIcon(d);
        }
    }

    public SortDir getDir() {
        return dir == SortDir.DESC ? SortDir.DESC : SortDir.ASC;
    }

    private void setIcon(SortDir d) {
        setIcon(d == SortDir.DESC ? ICO_DESC : ICO_ASC);
    }

    @Override
    protected void onClick(Event event) {
        super.onClick(event);
        setDir(dir == SortDir.ASC ? SortDir.DESC : SortDir.ASC);
    }

}
