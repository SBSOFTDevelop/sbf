package ru.sbsoft.meta.context;

/**
 * Контекст переменных в SQL запросе.
 * Служит для формирования общего контекста SQL запросов {@link ru.sbsoft.common.jdbc.QueryContext}, 
 * который затем используется парсером построителя запросов {@link ru.sbsoft.dao.AbstractBuilder} для
 * подстановки параметров в формируемый SQL запрос.
 * 
 * <p> Различные контексты -наследники {@link ru.sbsoft.meta.context.AbstractQueryContext} могут
 * связываться в единый контекст через метод {@link ru.sbsoft.meta.context.AbstractQueryContext#setParent(ru.sbsoft.common.jdbc.QueryContext)}
 * <p>Например:
 * <pre>
 * protected QueryContext createContext(String username, String context, PageFilterInfo filterInfo) {
 *       final QueryContext globalContext = new NSIGlobalContext(username, gridDaoBean, applicationName);
 *       final QueryContext templateContext = new TemplateContext(template).setParent(globalContext);
 *       final QueryContext filtersContext = new FiltersContext(context, getFilterDao(), filterInfo).setParent(templateContext);
 *       final QueryContext variableContext = new VarContext().setParent(filtersContext);
 *       return variableContext;
 *   }
 * </pre>
 * @author balandin
 * @since May 6, 2014 12:50:30 PM
 */
public class VarContext extends AbstractQueryContext {

	public VarContext() {
	}
}
