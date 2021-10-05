package ru.sbsoft.system.cfg;

import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.filter.FilterBox;

/**
 *
 * @author Kiselev
 */
public class GetDefaultFilterCommand extends ComplexFilterCommand {

    public GetDefaultFilterCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public FilterBox exec() {
        return getFilter(getCurrentFilterPath(getSysObject(true)));
    }
}
