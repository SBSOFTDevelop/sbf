package ru.sbsoft.system.cfg;

import java.util.Objects;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.grid.condition.GridCustomState;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.system.grid.SYS_OBJECT;

/**
 *
 * @author Kiselev
 */
public abstract class ComplexFilterCommand extends ConfigCommand {

    protected ComplexFilterCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    protected StoredFilterPath getCurrentFilterPath(SYS_OBJECT sysObject) {
        GridCustomState state = getCustomState(sysObject);
        return state != null ? state.getCurrentFilter() : null;
    }

    protected void setCurrentFilterPath(SYS_OBJECT sysObject, StoredFilterPath filterPath) {
        GridCustomState state = getCustomState(sysObject);
        if (state == null) {
            state = new GridCustomState();
        }
        state.setCurrentFilter(filterPath);
        putCustomState(sysObject, state);
    }
    
    protected boolean equals(StoredFilterPath p1, StoredFilterPath p2){
        return Objects.equals(norm(p1), norm(p2));
    }
    
    protected StoredFilterPath norm(final StoredFilterPath p){
        return p != null && (norm(p.getIdentityName()) != null || norm(p.getFilterName()) != null) ? p : null;
    }
    
    protected String norm(String s){
        return s != null && !(s = s.trim()).isEmpty() ? s : null;
    }

    protected FilterBox getFilter(StoredFilterPath path) {
        FiltersBean filter = new LoadFilterConfigCommand(context, getApplicationPrefixStr(), gridContext).exec(path);
        return new FilterBox(filter, path);
    }

    protected void setFilter(FilterBox f) {
        new SaveFilterConfigCommand(context, getApplicationPrefixStr(), gridContext).exec(f.getFilter(), f.getPath());
    }
}
