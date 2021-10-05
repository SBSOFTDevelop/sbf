package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.interfaces.ReportableItem;
import ru.sbsoft.shared.interfaces.SecureDynamicType;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Формирование грида списка браузеров
 * @author sokolov
 */
@PermitAll
public abstract class AbstractGridListGridDaoBean implements IGridListGridDao {

    @EJB
    private IGridDao gridDao;

    @EJB
    private Ii18nDao i18nDao;

    private final String DEFAULT_LOCALE = "ru_RU";

    private final Map<BigDecimal, ObjectType> gridItems = new HashMap<>();

    private Columns cashedColumns;

    @Override
    public Columns getMeta(GridContext context) {
        cashedColumns = MetaDataBuilder.getMeta(new GridListBrowserTemplate());
        return cashedColumns;
    }

    @Override
    public PageList<? extends MarkModel> getDataForBrowser(GridContext context, PageFilterInfo pageFilterInfo) throws FilterRequireException {
        String locale = getLocale(pageFilterInfo);
        PageList<Row> pageList = new PageList<>();
        List<Row> rows = getListRow(context, pageFilterInfo, locale);
        rows.forEach(row -> pageList.add(row));
        pageList.setTotalSize(rows.size());
        return pageList;
    }

    @Override
    public List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo pageFilterInfo, String idColumn) {
        throw new UnsupportedOperationException("Unsupported");
    }

    @Override
    public MarkModel getRow(GridContext context, PageFilterInfo pageFilterInfo, BigDecimal recordUQ) {
        return getDataRow(context, getLocale(pageFilterInfo), recordUQ, gridItems.get(recordUQ));
    }

    protected void initData() {
        int id = 1;
        for (String code : gridDao.getGridTemplatesCode()) {
            Enum e = Enum.valueOf(getGridTypeClass(), code);
            if (isRepotable(e)) {
                gridItems.put(BigDecimal.valueOf(id), (ObjectType) e);
                id += 1;
            }
        }
        for (String code : gridDao.getDynGridTemplatesCode()) {
            Enum e = Enum.valueOf(getDynGridTypeClass(), code);
            if (isRepotable(e)) {
                gridItems.put(BigDecimal.valueOf(id), (ObjectType) e);
                id += 1;
            }
        }
    }

    private List<Row> getListRow(GridContext context, PageFilterInfo pageFilterInfo, String locale) {
        List<Row> list = new ArrayList<>();
        gridItems.forEach((id, gridType) -> list.add(getDataRow(context, locale, id, gridType)));
        if (Dict.CGRID_CODE.equals(getSortColumn(pageFilterInfo))) {
            list.sort((row1, row2) -> {
                String code1 = (String) row1.getString(Dict.CGRID_CODE);
                String code2 = (String) row2.getString(Dict.CGRID_CODE);
                return code1.compareTo(code2);
            });
        } else {
            list.sort((row1, row2) -> {
                String title1 = (String) row1.getString(Dict.CGRID_NAME);
                String title2 = (String) row2.getString(Dict.CGRID_NAME);
                return title1.compareTo(title2);
            });
        }
        return list;
    }

    private Row getDataRow(GridContext context, String locale, BigDecimal id, ObjectType gridType) {
        if (null == cashedColumns) {
            getMeta(context);
        }
        List tmp = new ArrayList();
        final Row result = new Row();
        result.setColumns(cashedColumns);
        tmp.add(id);
        result.setRECORD_ID(id);
        tmp.add(getNameType(gridType));
        tmp.add(gridType.getCode());
        if (gridType instanceof NamedItem) {
            tmp.add(i18nDao.get(locale, ((NamedItem) gridType).getItemName()));
        }
        result.setValues(tmp);
        return result;
    }

    private String getSortColumn(PageFilterInfo pageFilterInfo) {
        if (pageFilterInfo.getSorts().isEmpty()) {
            return "";
        }
        return pageFilterInfo.getSorts().get(0).getAlias();
    }

    private String getLocale(PageFilterInfo pageFilterInfo) {
        for (FilterInfo filterInfo : pageFilterInfo.getParentFilters()) {
            if (filterInfo instanceof StringFilterInfo && Dict.LOCALE.equals(filterInfo.getColumnName())) {
                return ((StringFilterInfo) filterInfo).getValue();
            }
        }
        return DEFAULT_LOCALE;
    }
    
    private String getNameType(ObjectType gridType) {
        if (gridType instanceof SecureDynamicType) {
            return Dict.CGRID_TYPE_DYN;
        }
        return Dict.CGRID_TYPE_NORM;
    }
    
    private boolean isRepotable(Enum e) {
        if (e instanceof ReportableItem) {
            return ((ReportableItem) e).isReportable();
        }
        return false;
    }

    protected abstract Class getGridTypeClass();

    protected abstract Class getDynGridTypeClass();

}
