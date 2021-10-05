package ru.sbsoft.shared.meta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sychugin
 */
public class RowsInfo implements IColumnCustomInfo {

    private  List<BigDecimal> rows = new ArrayList<>();

    public RowsInfo() {
    }

    public void addRow(BigDecimal row) {
        rows.add(row);
    }

    public List<BigDecimal> getItems() {
        return rows;
    }
}
