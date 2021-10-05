package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ru.sbsoft.shared.meta.UpdateInfo;
import ru.sbsoft.shared.meta.Wrapper;

public interface IGridUpdateDao {

    BigDecimal updateColumn(BigDecimal primaryKey, UpdateInfo info, Object value);

    List<BigDecimal> updateColumn(List<BigDecimal> primaryKeys, UpdateInfo info, Object value);

    List<BigDecimal> updateRows(Map<BigDecimal, Map<String, Wrapper>> rows, String table);

}
