package ru.sbsoft.common.jdbc;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIME;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author balandin
 * @since May 6, 2014 2:19:57 PM
 */
public class QueryParamImpl implements QueryParam {

	private final Object value;
	private final TemporalType temporalType;

	public QueryParamImpl(final Object value) {
		this(value, null);
	}

	public QueryParamImpl(final Object value, final TemporalType temporalType) {

		if (value instanceof Date) {
			if (temporalType == null) {
				// throw new IllegalArgumentException();
			}
		}

		this.value = value;
		this.temporalType = temporalType;
	}

	public Object getValue() {
		return value;
	}

	public TemporalType getTemporalType() {
		return temporalType;
	}

	public void setQueryParameter(Query query, int index) {
		if ((value instanceof Date) && (temporalType != null)) {
			query.setParameter(index, (Date) value, temporalType);
			return;
		}
		query.setParameter(index, value);
	}

	public void setQueryParameter(Query query, String name) {
		if ((value instanceof Date) && (temporalType != null)) {
			query.setParameter(name, (Date) value, temporalType);
		} else {
			query.setParameter(name, value);
		}
	}

	public void setJdbcParameter(PreparedStatement statement, int index) throws SQLException {
		if (value instanceof Date) {
			switch (temporalType) {
				case DATE:
					statement.setDate(index, new java.sql.Date(((Date) value).getTime()));
					break;
				case TIME:
					statement.setTime(index, new java.sql.Time(((Date) value).getTime()));
					break;
				case TIMESTAMP:
					statement.setTimestamp(index, new java.sql.Timestamp(((Date) value).getTime()));
					break;
				default:
					throw new IllegalStateException();
			}
		} else if (value instanceof String) {
			statement.setString(index, (String) value);
		} else if (value instanceof Byte) {
			statement.setByte(index, (Byte) value);
		} else if (value instanceof Short) {
			statement.setInt(index, (Short) value);
		} else if (value instanceof Integer) {
			statement.setInt(index, (Integer) value);
		} else if (value instanceof Long) {
			statement.setLong(index, (Long) value);
		} else if (value instanceof Float) {
			statement.setFloat(index, (Float) value);
		} else if (value instanceof Double) {
			statement.setDouble(index, (Double) value);
		} else if (value instanceof BigDecimal) {
			statement.setBigDecimal(index, (BigDecimal) value);
		} else if (value instanceof Boolean) {
			statement.setBoolean(index, (Boolean) value);
		} else {
			throw new SQLException("Unexpected param type " + ((value == null) ? "null" : value.getClass().getName()));
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
}
