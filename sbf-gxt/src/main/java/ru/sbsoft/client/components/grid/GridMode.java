package ru.sbsoft.client.components.grid;

/**
 * Типы операций с данными в таблице.
 * @author balandin
 * @since Apr 12, 2013 1:19:04 PM
 */
public enum GridMode {

    SINGLE_SELECTION, HIDE_INSERT, HIDE_UPDATE, HIDE_DELETE, FORCED_FILTER, HIDE_ONLYUPDATE, HIDE_MOVE, HIDE_FILTER, SHOW_GRID_RELOAD, ADD_CLONE, HIDE_REPORTS;
    //
    public static GridMode[] SET_SINGLE_SELECTION = new GridMode[]{SINGLE_SELECTION};
    public static GridMode[] SET_EMPTY = new GridMode[]{};

    public static GridMode[] getSingleSet(final boolean value) {
        return value ? SET_SINGLE_SELECTION : SET_EMPTY;
    }
}
