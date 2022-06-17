package ru.sbsoft.client.components.grid;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Вспомогательный класс для работы с {@link GridMode}
 * @author balandin
 * @since Feb 20, 2015 6:32:14 PM
 */
public class GridModes {

    private final Set<GridMode> items;

    private GridModes(GridMode... modes) {
        this.items = (modes == null || modes.length == 0) ? EnumSet.noneOf(GridMode.class) : EnumSet.copyOf(Arrays.asList(modes));
    }

    public static GridModes build(GridMode... modes) {
        return new GridModes(modes);
    }

    public GridModes addIf(boolean condition, GridMode mode) {
        if (condition) {
            items.add(mode);
        }
        return this;
    }

    public Set<GridMode> asSet() {
        return Collections.unmodifiableSet(items);
    }

    public final boolean isHideMove() {
        return items.contains(GridMode.HIDE_MOVE);
    }
    
    public final boolean isHideFilter() {
        return items.contains(GridMode.HIDE_FILTER);
    }
        
    public final boolean isHideInsert() {
        return items.contains(GridMode.HIDE_INSERT);
    }
        
    public final boolean isAddClone() {
        return items.contains(GridMode.ADD_CLONE);
    }
    
    public final boolean isHideSave() {
        return items.contains(GridMode.HIDE_SAVE);
    }

    public final boolean isHideOnlyUpdate() {
        return items.contains(GridMode.HIDE_ONLYUPDATE);
    }

    public final boolean isHideUpdate() {
        return items.contains(GridMode.HIDE_UPDATE);
    }

    public final boolean isHideDelete() {
        return items.contains(GridMode.HIDE_DELETE);
    }

    public final boolean isSingleSelect() {
        return items.contains(GridMode.SINGLE_SELECTION);
    }

    public final boolean isForcedFilter() {
        return items.contains(GridMode.FORCED_FILTER);
    }

    public final boolean isMultiSelect() {
        return !isSingleSelect();
    }
    
    public final boolean isShowGridReload(){
        return items.contains(GridMode.SHOW_GRID_RELOAD);
    }
}
