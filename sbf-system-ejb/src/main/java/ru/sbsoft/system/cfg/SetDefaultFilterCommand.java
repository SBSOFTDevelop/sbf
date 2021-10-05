package ru.sbsoft.system.cfg;

import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;

/**
 *
 * @author Kiselev
 */
public class SetDefaultFilterCommand extends ComplexFilterCommand {

    public SetDefaultFilterCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public void exec(StoredFilterPath filterPath) {
        setCurrentFilterPath(getSysObject(true), filterPath);
    }
}
