package ru.sbsoft.dao;

/**
 * Предоставляет опосредованную информацию об sql template.
 *
 * @author Kiselev
 * @see ru.sbsoft.meta.sql.SQLBuilder
 */
public interface ISqlTemplateInfo extends IMetaTemplateInfo {

    void buildFromClause(StringBuilder sb);

    void buildWhereClause(StringBuilder sb);

}
