package ru.sbsoft.meta.columns.style.condition;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import ru.sbsoft.operation.kladr.DBFReader;
import java.util.Map;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.meta.columns.ColumnInfo;
import static ru.sbsoft.meta.columns.consts.ColumnDefinitions.*;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.Row;

public class ConditionTest {

    private final ColumnInfo<BigDecimal> colId;
    private final ColumnInfo<Long> colInt1;
    private final ColumnInfo<String> colStr1;
    private final ColumnInfo<Long> colInt2;
    private final ColumnInfo<String> colStr2;
    private final Columns columns;

    private final Row r1;
    private final Row r2;
    private final Row r3;
    private final Row r4;
    private final List<Row> rows = new ArrayList<>();

    public ConditionTest() {
        ColumnsInfo c = new ColumnsInfo();
        colId = c.add(KEY, "t.id");
        colInt1 = c.add(INTEGER, 100, "Test int 1", "t.int1");
        colStr1 = c.add(VCHAR, 100, "Test str 1", "t.str1");
        colInt2 = c.add(INTEGER, 100, "Test int 2", "t.int2");
        colStr2 = c.add(VCHAR, 100, "Test str 2", "t.str2");
        columns = c.getColumns();
        r1 = addRow(1, 1, "str1", 2, "str2");
        r2 = addRow(2, 11, null, 22, "str22");
        r3 = addRow(3, 111, null, 222, "str222");
        r4 = addRow(4, 1111, "str1111", 2222, null);
    }

    private Row addRow(Integer key, Integer i1, String s1, Integer i2, String s2) {
        Row r = new Row();
        List<Object> values = new ArrayList<>();
        values.add(new BigDecimal(key));
        values.add(i1);
        values.add(s1);
        values.add(i2);
        values.add(s2);
        r.setColumns(columns);
        r.setValues(values);
        rows.add(r);
        return r;
    }

    @Test
    public void testComplexCondition() throws Exception {
        System.out.println("testComplexCondition");
        String disableVal = "str1111";
        Set<String> disables = new HashSet<>();
        disables.add(disableVal);
        ColumnInfo<?> col = colStr1;
        String alias = col.getAlias();
        Exp condFactory = new Exp(new NotNull(col));
        condFactory.and(new NotIn(col, disables));
        IGridCondition cond = condFactory.createCondition();
        for(Row row : rows){
            Object val = row.getValue(alias);
            assertEquals(cond.isMatch(row), val != null && !disables.contains(val));
        }
    }
}
