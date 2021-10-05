package ru.sbsoft.client.components.grid;

import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;

/**
 *
 * @author vk
 * @param <M>
 */
public abstract class ColViewConfig<M> implements GridViewConfig<M> {

    public ColViewConfig() {
        this(null);
    }

    public ColViewConfig(CssResource res) {
        if (res != null) {
            res.ensureInjected();
        }
    }

    @Override
    public String getRowStyle(M model, int rowIndex) {
        return "";
    }
}
