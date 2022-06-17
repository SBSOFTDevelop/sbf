package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ru.sbsoft.shared.meta.Wrapper;

public interface IGridUpdateDao {

    List<BigDecimal> updateRows(String table, Map<BigDecimal, Map<String, Wrapper>> rows);
}
