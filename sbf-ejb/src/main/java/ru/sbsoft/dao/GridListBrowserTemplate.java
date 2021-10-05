package ru.sbsoft.dao;

import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.Dict;

/**
 * Темплейт грида списка браузеров
 * @author sokolov
 */
public class GridListBrowserTemplate extends CommonTemplate {

    public GridListBrowserTemplate() {
        setDefaultSort(Dict.CGRID_CODE);
    }

    @Override
    protected ColumnsInfo createColumns() {
        ColumnsInfo c = new ColumnsInfo();
        c.add(KEY, "id");
        c.add(VCHAR, 200, "type", Dict.CGRID_TYPE).setVisible(false);
        c.add(VCHAR, 200, SBFGeneralStr.titleCode, Dict.CGRID_CODE);
        c.add(VCHAR, 600, SBFGeneralStr.titleName, Dict.CGRID_NAME).setAutoExpand(true);
        return c;
    }

}
