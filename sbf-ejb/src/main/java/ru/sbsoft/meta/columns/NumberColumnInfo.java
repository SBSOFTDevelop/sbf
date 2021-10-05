package ru.sbsoft.meta.columns;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import ru.sbsoft.shared.meta.ColumnType;

/**
 * Абстрактный базовый класс для всех числовых колонок.
 * Переопределяет метод read супер класса.
 * @author balandin
 * @since Jun 6, 2014 1:59:46 PM
 */
public abstract class NumberColumnInfo extends ColumnInfo<BigDecimal> {

	public NumberColumnInfo(ColumnType type) {
		super(type);
	}
	
	@Override
	public BigDecimal read(ResultSet resultSet) throws SQLException {
		return resultSet.getBigDecimal(alias);
	}

	@Override
	public String getXlsTypeHint() {
		return "x:num";
	}
}
