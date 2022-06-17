package ru.sbsoft.shared.meta;

import java.util.List;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.FilterDefinitions;
import ru.sbsoft.shared.meta.filter.FilterTemplate;
import ru.sbsoft.shared.model.SortingInfo;

/**
 *
 * @author vk
 */
public interface IColumns {

    int frozenWidth();

    IColumn getAutoExpanColumn();

    String getCaption();

    IColumn getColumnForAlias(String alias);

    List<IColumn> getColumns();

    IGridCustomInfo getCustomInfo();
    
    boolean isSignPositive();

    IGridCondition getEditCondition();

    FilterDefinitions getFilterDefinitions();

    FilterTemplate getFilterTemplate();

    IColumn getFrozenAutoExpanColumn();

    List<ConditionalCellStyle> getGridStyles();

    IColumn getPrimaryKey();

    List<SortingInfo> getSort();

    List<Style> getStyles();

    String getTemplateClassName();

    IColumn getTemporalKey();

    String getUpdateTableName();

    boolean hasExpCellFormat();

    boolean hasFrozen();

    boolean hasStyles();
    
    void applyColumnConfig(ColumnCfg config);
    
    void replaceStyles(List<ConditionalCellStyle> gridStyles);

    boolean isRowStyleDominate();
}
