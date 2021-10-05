package ru.sbsoft.meta.columns;

import java.math.BigDecimal;
import ru.sbsoft.shared.meta.BigDecimalWrapper;
import ru.sbsoft.shared.meta.StringWrapper;
import ru.sbsoft.shared.meta.Wrapper;

/**
 * @author balandin
 * @since May 16, 2014 3:57:18 PM
 */
public class ExpressionColumnFooter extends AbstractColumnFooter {

	private final String expression;

	public String getExpression() {
		return expression;
	}

	public ExpressionColumnFooter(String expression) {
		this.expression = expression;
	}

	@Override
	public String build(String clause) {
		return expression;
	}

	@Override
	public Wrapper wrap(Object value) {
		if (value instanceof String) {
			return new StringWrapper((String) value);
		}
		if (value instanceof BigDecimal) {
			return new BigDecimalWrapper((BigDecimal) value);
		}
		throw new IllegalArgumentException("Unsupported type " + value.getClass().getName());
	}
}
