package ru.sbsoft.svc.widget.core.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author Kiselev
 */
public class SvcGridResource {

    private static final ColumnHeader.ColumnHeaderAppearance COLUMN_HEADER_APPEARANCE = GWT.<ColumnHeader.ColumnHeaderAppearance>create(ColumnHeader.ColumnHeaderAppearance.class);

    /**
     * Returns the icon to use for the "Sort Ascending" header menu item.
     *
     * @return the sort ascending menu icon
     */
    public static ImageResource getSortAscendingIcon() {
        return COLUMN_HEADER_APPEARANCE.sortAscendingIcon();
    }

    /**
     * Returns the icon to use for the "Sort Descending" header menu item.
     *
     * @return the sort descending menu icon
     */
    public static ImageResource getSortDescendingIcon() {
        return COLUMN_HEADER_APPEARANCE.sortDescendingIcon();
    }
}
