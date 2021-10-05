package ru.sbsoft.meta.context;

import ru.sbsoft.meta.sql.SQLBuilder;

/**
 *
 * @author Kiselev
 */
public interface ISQLBuilderFactory {

    SQLBuilder createSQLBuilder();
}
