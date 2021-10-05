package ru.sbsoft.meta.context;

import ru.sbsoft.dao.ISqlTemplateInfo;
import ru.sbsoft.meta.sql.SQLBuilder;

/**
 *
 * @author Kiselev
 */
public class DefaultSQLBuilderFactory implements ISQLBuilderFactory {
    private final ISqlTemplateInfo sqlTemplateInfo;

    public DefaultSQLBuilderFactory(ISqlTemplateInfo sqlTemplateInfo) {
        this.sqlTemplateInfo = sqlTemplateInfo;
    }
    
    @Override
    public SQLBuilder createSQLBuilder() {
        return new SQLBuilder(sqlTemplateInfo);
    }
}
