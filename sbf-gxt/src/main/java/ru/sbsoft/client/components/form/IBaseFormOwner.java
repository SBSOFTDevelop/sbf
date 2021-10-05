package ru.sbsoft.client.components.form;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FilterInfo;

/**
 *
 * @author Kiselev
 */
public interface IBaseFormOwner {

    boolean isReadOnly(boolean deep);

    void refreshRow(IFormModel model);

    boolean isInitialized();

    List<FilterInfo> getParentFilters();

    List<FilterInfo> getDefinedFilters();

    BigDecimal getClonableRecordID();

    void restoreFocus();
}
