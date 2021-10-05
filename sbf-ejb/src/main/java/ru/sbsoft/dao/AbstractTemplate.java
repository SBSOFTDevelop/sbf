package ru.sbsoft.dao;

/**
 *
 * Базовый интерфейс для формирования запросов браузера
 *
 * @author balandin
 * @since Apr 1, 2014 3:51:14 PM
 */
public abstract class AbstractTemplate extends CommonTemplate implements ISqlTemplateInfo {

    public AbstractTemplate() {
    }

    @Override
    public abstract void buildFromClause(StringBuilder sb);

    @Override
    public void buildWhereClause(StringBuilder sb) {
    }

}
