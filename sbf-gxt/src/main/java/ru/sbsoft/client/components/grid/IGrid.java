package ru.sbsoft.client.components.grid;

import java.util.Collection;
import ru.sbsoft.client.components.IElement;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.sbf.app.model.FormModel;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IGrid extends IElement {

    boolean isInitialized();

    void refreshRow(FormModel model);

    Collection<? extends FilterInfo> getParentFilters();

    Collection<? extends FilterInfo> getDefinedFilters();

    boolean isReadOnly();

    boolean isReadOnly(boolean deep);

    void setReadOnly(boolean readOnly);

    void setChanged(boolean changed);

    boolean isChanged();

    void loadAddedRow(FormModel model);
}
