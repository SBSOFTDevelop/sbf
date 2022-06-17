package ru.sbsoft.meta.lookup;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.EntityManager;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * Базовый абстрактный класс представляет поставщика данных для экземпляров {@link ru.sbsoft.shared.model.LookupInfoModel}.
 * @author balandin
 * @since Feb 24, 2015 1:24:59 PM
 */
public abstract class LookupValueProvider {

    public static final String CODE_VALUE = "CODE_VALUE";
    public static final String NAME_VALUE = "NAME_VALUE";
    public static final LookupValueProvider DEFAULT = new RowLookupValueProvider(CODE_VALUE, NAME_VALUE);
    //
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final String DATA_NOT_FOUND = "???";
    //
    protected final String codeColumn;
    protected final String nameColumn;
    protected final String semanticKeyColumn;
    private Class<? extends LookupInfoModel> lookupModelClass;
    //
    private boolean nameCaseSensitive = false;

    public abstract LookupInfoModel createLookupModel(Row row, EntityManager entityManager);

    public LookupValueProvider setLookupModelClass(Class<? extends LookupInfoModel> lookupModelClass) {
        this.lookupModelClass = lookupModelClass;
        return this;
    }

    protected LookupInfoModel createLookupModelInstance() {
        try {
            if (lookupModelClass == null) {
                lookupModelClass = LookupInfoModel.class;
            }
            return (LookupInfoModel) lookupModelClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
        } catch (Exception ex) {
            throw new ApplicationException(SBFExceptionStr.classNotDefConstructor, lookupModelClass.getCanonicalName());
        }
    }

    public LookupValueProvider(String codeColumn, String nameColumn, String semanticKeyColumn) {
        super();
        this.codeColumn = codeColumn;
        this.nameColumn = nameColumn;
        this.semanticKeyColumn = semanticKeyColumn;
    }

    public FilterInfo convertLookupFilterItem(StringFilterInfo lookupInfo) {
        final FilterTypeEnum type = lookupInfo.getType();
        if (type == null) {
            throw new IllegalArgumentException();
        }
        
        switch (type) {
            case LOOKUP_CODE:
                return createKeyLookupFilter(lookupInfo.getValue());
            case LOOKUP_NAME:
                return createNameLookupFilter(lookupInfo.getValue());
        }

        throw new IllegalArgumentException("unexpected cellType = " + type);
    }

    protected FilterInfo createKeyLookupFilter(String query) {
        final StringFilterInfo filterInfo = new StringFilterInfo(codeColumn, ComparisonEnum.startswith, query);
        filterInfo.setCaseSensitive(nameCaseSensitive);
        return filterInfo;
    }

    protected FilterInfo createNameLookupFilter(String query) {
        final StringFilterInfo filterInfo = new StringFilterInfo(nameColumn, ComparisonEnum.startswith, query);
        filterInfo.setCaseSensitive(nameCaseSensitive);
        return filterInfo;
    }

    // 
    protected String convertKey(Object value) {
        if (value instanceof String) {
            value = Strings.clean((String) value, true);
        }
        if (value == null) {
            return DATA_NOT_FOUND;
        }
        return String.valueOf(value);
    }

    protected String convertName(Object value) {
        if (value instanceof String) {
            value = Strings.clean((String) value, true);
        }
        if (value == null) {
            return DATA_NOT_FOUND;
        }
        if (value instanceof Date) {
            return foramtDate((Date) value);
        }
        return String.valueOf(value);
    }

    protected String foramtDate(Date value) {
        return DATE_FORMAT.format(value);
    }

    public LookupValueProvider setNameCaseSensitive(boolean value) {
        this.nameCaseSensitive = value;
        return this;
    }

    protected Long parseLong(String query) {
        try {
            return Long.parseLong(query);
        } catch (NumberFormatException ignore) {
            throw new ApplicationException(SBFExceptionStr.errorConvertingDecimal, query);
        }
    }
}
