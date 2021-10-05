package ru.sbsoft.meta.context;

import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.common.jdbc.QueryParamImpl;
import ru.sbsoft.dao.AbstractTemplate;

/**
 * Класс представляет контекст шаблона.
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
 * 
 * @author balandin
 * @since May 5, 2014 8:08:26 PM
 */
public class TemplateContext extends AbstractQueryContext {

	private final AbstractTemplate template;

	public TemplateContext(final AbstractTemplate template) {
		this.template = template;
	}

	public AbstractTemplate getTemplate() {
		return template;
	}

	@Override
	public QueryParam getParam(String name) {
		final Object param = template.getParameter(name);
		if (param != null) {
			return new QueryParamImpl(param);
		}
		return null;
	}
}
