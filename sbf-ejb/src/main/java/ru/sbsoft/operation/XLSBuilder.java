package ru.sbsoft.operation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.sbsoft.common.Strings;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.shared.api.i18n.I18nResource;
import ru.sbsoft.shared.consts.Formats;
import ru.sbsoft.shared.meta.ColumnGroup;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.renderer.Renderer;
import ru.sbsoft.shared.renderer.RendererManager;

/**
 * Класс реализует выгрузку табличных данных в Excel.
 *
 * @author Sokoloff
 */
public class XLSBuilder {

    private final int MAX_WIDTH = 65280;

    protected final RendererManager rendererManager;
    private HashMap<String, DateFormat> cache;
    private final File file;
    protected final I18nResource i18nResource;

    private final Workbook workbook;
    private Sheet sheet;
    protected int rownum = 1;

    // private int bRow;
    private int bCol;

    private final Map<Integer, CellStyle> styles = new HashMap<>();
    protected CellStyle[] cellStyles;

    private final CellStyle headerStyle;

    public XLSBuilder(RendererManager rendererManager, File file, I18nResource i18nResource) {

        this(new XSSFWorkbook(), rendererManager, file, i18nResource);

    }

    public XLSBuilder(Workbook workbook, RendererManager rendererManager, File file, I18nResource i18nResource) {
        this.rendererManager = rendererManager;
        this.workbook = workbook;
        this.file = file;
        this.i18nResource = i18nResource;
        //this.sheet = workbook.createSheet();
        headerStyle = workbook.createCellStyle();
        setBorder(headerStyle);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setWrapText(true);
        headerStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public Sheet getSheet() {
        return sheet;
    }

    public XLSBuilder setBeginData(String cellRef) {
        CellReference cr = new CellReference(cellRef);
        setSheet(cr.getSheetName());
        rownum = cr.getRow();
        bCol = cr.getCol();
        return this;
    }

    public XLSBuilder setTitle(String title, String cellRef) {

        CellReference titleRef = new CellReference(cellRef);

        setSheet(titleRef.getSheetName());
        Row r = getRow(titleRef.getRow());
        Cell t = r.createCell(titleRef.getCol());
        t.setCellValue(title);

        return this;

    }

    public XLSBuilder setRow(int row) {
        this.rownum = row;
        return this;
    }

    public XLSBuilder setCol(int col) {

        this.bCol = col;
        return this;
    }

    public XLSBuilder setSheet(String name) {
        if (name != null) {
            this.sheet = workbook.getSheet(name);
        } else {
            this.sheet = workbook.getSheetAt(0);

        }
        return this;
    }

    public void header(List<ColumnInfo> meta) {

        if (sheet == null) {
            this.sheet = workbook.createSheet();
        }

        HeadItemsBuilder hb = new HeadItemsBuilder().build(meta);

//заголовки
        List<HeadItem> level;
        while ((level = hb.getNextLevel()) != null) {
            HeaderCellBuilder rb = new HeaderCellBuilder(getRow(rownum++));

            for (HeadItem it : level) {

                rb.move(it.getColNum() + bCol)
                        .headC(it.getName()).merge(it.getRowSpan(), it.getColSpan());

            }

        }
        setStyles(meta);

    }

    private void setStyles(List<ColumnInfo> meta) {
        if (cellStyles != null) {
            return;
        }
        cellStyles = new CellStyle[meta.size()];
        for (int i = 0; i < meta.size(); i++) {
            cellStyles[i] = getStyle(meta.get(i));
            sheet.setColumnWidth(i + bCol, (getWidth(meta.get(i))));
        }

    }

    private void setBorder(CellStyle style) {

        short black = IndexedColors.BLACK.getIndex();
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(black);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(black);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(black);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(black);

    }

    private CellStyle getStyle(ColumnInfo ci) {
        String format = getFormat(ci);
        HorizontalAlignment ha = getColumnAlign(ci);

        int key = (format + ha.name()).hashCode();
        CellStyle style = styles.get(key);
        if (style == null) {
            CreationHelper creationHelper = workbook.getCreationHelper();
            style = workbook.createCellStyle();
            style.setAlignment(ha);
            style.setDataFormat(creationHelper.createDataFormat().getFormat(format));
            setBorder(style);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            styles.put(key, style);
        }

        return style;
    }

    protected Cell getCell(int colnum, Row row) {
        Cell cell = row.getCell(colnum);

        return cell != null ? cell : row.createCell(colnum);
    }

    protected Row getRow(int rownum) {
        Row row = sheet.getRow(rownum);
        return row != null ? row : sheet.createRow(rownum);
    }

    private final class HeaderCellBuilder {

        private final Row row;

        private int nextCol = 0;
        private Cell cell = null;

        private HeaderCellBuilder(Row row) {
            this.row = row;

        }

        public HeaderCellBuilder move(int col) {
            this.nextCol = col;
            return this;
        }

        public HeaderCellBuilder shift(int colShift) {
            move(nextCol - 1 + colShift);
            return this;
        }

        public HeaderCellBuilder merge(int rowspan, int colspan) {
            if (rowspan > 1 || colspan > 1) {
                if (colspan > 1) {
                    shift(colspan);
                }
                CellStyle style = cell.getCellStyle();
                for (int i = 1; i < rowspan; i++) {
                    Row r = getRow(cell.getRowIndex() + i);

                    getCell(cell.getColumnIndex(), r).setCellStyle(style);

                }
                for (int i = 1; i < colspan; i++) {
                    getCell(cell.getColumnIndex() + i, row).setCellStyle(style);
                }

                sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum() + rowspan - 1, cell.getColumnIndex(), nextCol - 1));
            }
            return this;
        }

        private void nextCell() {
            cell = getCell(nextCol++, row);
        }

        public HeaderCellBuilder headC(String val) {
            nextCell();
            cell.setCellType(CellType.STRING);
            cell.setCellStyle(headerStyle);
            if (val != null) {
                cell.setCellValue(val);
            }
            return this;

        }

    }

    private static class HeadItem {

        private final String name;
        private HeadItem parent;
        private int level = -1;
        private int colNum = -1;
        private int rowSpan = 1;
        private int colSpan = 0;

        public HeadItem(String name) {
            this.name = name;

        }

        public void setParent(HeadItem parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public void incColSpan() {
            this.colSpan++;

        }

        public HeadItem getParent() {
            return parent;
        }

        public int getLevel() {
            return level;
        }

        public int getRowSpan() {
            return rowSpan;
        }

        public int getColSpan() {
            return colSpan;
        }

        public int getColNum() {
            return colNum;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setColNum(int colNum) {
            this.colNum = colNum;
        }

        public void setRowSpan(int rowSpan) {
            this.rowSpan = rowSpan;
        }

        public void setColSpan(int colSpan) {
            this.colSpan = colSpan;
        }

    }

    public static class HeadItemsBuilder {

        private Map<ColumnGroup, HeadItem> groups = new HashMap<>();

        private int levelSize = 0;
        private int level = -1;
        private List<HeadItem> headItems;

        public HeadItemsBuilder build(List<ColumnInfo> meta) {

            headItems = new ArrayList<>(meta.size());
            int i = 0;
            for (ColumnInfo col : meta) {
               
                
                HeadItem item = new HeadItem(col.getCaption().getDefaultName());
                item.setColSpan(1);
                item.setColNum(i++);
                fillParent(item, col);
                headItems.add(item);

            }
            levelSize = fillLevel();
            return this;
        }

        public int getHeadRowsCount() {
            if (headItems == null) {
                throw new RuntimeException("HeadItemsBuilder: first use the build method");
            }
            int rows = 0;

            HeadItem lastHead = headItems.stream().filter(t -> (t.level == levelSize)).findAny().get();

            do {
                rows += lastHead.getRowSpan();
                lastHead = lastHead.getParent();

            } while (lastHead != null);

            return rows;
        }

        private void fillParent(HeadItem item, ColumnInfo col) {
            ColumnGroup group = col.getGroup();
            while (group != null) {
                HeadItem parent = groups.get(group);

                if (parent == null) {
                    parent = new HeadItem(group.getTitle().getDefaultName());
                    groups.put(group, parent);
                }
                parent.incColSpan();
                parent.setColNum(parent.getColNum() < 0 ? item.getColNum() : Math.min(parent.getColNum(), item.getColNum()));
                item.setParent(parent);
                item = parent;
                group = group.getParent();
            }

        }

        private void setLevel(HeadItem item, int level) {
            if (item.getLevel() >= 0) {
                return;
            }
            HeadItem parent = item.getParent();
            if (parent == null) {
                item.setLevel(0);
                item.setRowSpan(level + 1);
            } else {
                setLevel(parent, level - 1);
                item.setLevel(parent.getLevel() + parent.getRowSpan());
                item.setRowSpan(level - item.getLevel() + 1);
            }
        }

        private int fillLevel() {
            List<Pair<HeadItem, Integer>> pathLengths = new ArrayList<>(headItems.size());
            for (HeadItem item : headItems) {
                int pathLen = 0;
                HeadItem pathItem = item.getParent();
                while (pathItem != null) {
                    pathLen++;
                    pathItem = pathItem.getParent();
                }
                pathLengths.add(Pair.of(item, pathLen));
            }
            Collections.sort(pathLengths, (l1, l2) -> Integer.compare(l2.getValue(), l1.getValue()));
            int maxLevel = pathLengths.get(0).getValue();
            for (Pair<HeadItem, Integer> pair : pathLengths) {
                HeadItem item = pair.getKey();
                setLevel(item, maxLevel);
            }
            return maxLevel;
        }

        public List<HeadItem> getNextLevel() {
            level++;
            if (level > levelSize) {
                return null;
            }
            List<HeadItem> res = new ArrayList<>();
            for (HeadItem it : headItems) {
                if (it.getLevel() == level) {
                    res.add(it);
                } else {
                    for (HeadItem parent = it.getParent(); parent != null; parent = parent.getParent()) {
                        if (parent.getLevel() == level) {
                            if (res.lastIndexOf(parent) < 0) {
                                res.add(parent);
                            }
                            break;
                        }
                    }
                }
            }
            Collections.sort(res, (it1, it2) -> Integer.compare(it1.getColNum(), it2.getColNum()));
            return res;
        }

    }

    public void row(List<ColumnInfo> meta, ResultSet resultSet) throws SQLException {
        setStyles(meta);

        Row row = getRow(rownum++);
        for (int i = 0; i < meta.size(); i++) {
            Cell cell = getCell(i + bCol, row);
            cell.setCellStyle(cellStyles[i]);
            cell.setCellValue(read(resultSet, meta.get(i)));
        }
    }

    public void close() throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
    }

    private int getWidth(ColumnInfo column) {
        int width = column.isVisible() || (column.getClause().contains("$SYSTEM_EXPORT")) ? column.getWidth() : 0;
        final ColumnType type = column.getType();
        switch (type) {
            case KEY:
            case TEMPORAL_KEY:
            case IDENTIFIER:
                width = 0;
            case INTEGER:

            case CURRENCY:
            case DATE:
            case DATE_TIME:
            case TIMESTAMP:
            case BOOL:
            case VCHAR:
            case ADDRESS:
            case YMDAY:
        }

        if (!column.isVisible()) {
            width = 0;
        }
        if (width > 0) {
            width = calculateColWidth(width);
        }
        return width;
    }

    private int calculateColWidth(int width) {
        if (width > 1) {
            int floor = (int) (Math.floor(((double) width) / 15));
            int factor = (30 * floor);
            int value = 450 + factor + ((width - 1) * 36);
            if (value > MAX_WIDTH) {
                return MAX_WIDTH;
            }
            return value;
        } else {
            return 450; // default to column size 1 if zero, one or negative number is passed.
        }
    }

    private String getFormat(ColumnInfo column) {
        final ColumnType type = column.getType();
        final String format = column.getFormat();
        switch (type) {
            case KEY:
            case INTEGER:
            case TEMPORAL_KEY:
            case IDENTIFIER:
                final Renderer renderer = rendererManager.getRenderer(format);
                if (renderer != null) {
                    break;
                }
                return Strings.coalesce(format, "0");
            case CURRENCY:
                return Strings.coalesce(format, "0.00");
            case DATE:
            case YMDAY:
                return getDateTimeFormat(format, "dd.MM.yyyy");
            case DATE_TIME:
            case TIMESTAMP:
                return getDateTimeFormat(format, "dd.MM.yyyy HH.mm.ss");
            case VCHAR:
            case ADDRESS:
            case BOOL:

        }
        return "";
    }

    private static String getDateTimeFormat(String format, String defValue) {
        if (Formats.DATE_SHORT.equals(format)) {
            return "dd.MM.yyyy";
        }
        if (Formats.DATE_TIME_SHORT.equals(format)) {
            return "dd.MM.yyyy HH.mm";
        }
        if (Formats.DATE_TIME_MEDIUM.equals(format)) {
            return "dd.MM.yyyy HH.mm.ss";
        }
        if (Formats.HOUR24_MINUTE.equals(format)) {
            return "HH.mm";
        }
        return Strings.coalesce(format, defValue);
    }

    private HorizontalAlignment getColumnAlign(ColumnInfo column) {
        final ColumnType type = column.getType();
        switch (type) {
            case KEY:
            case INTEGER:
            case TEMPORAL_KEY:
            case IDENTIFIER:
            case CURRENCY:
                return HorizontalAlignment.RIGHT;
            case DATE:
            case YMDAY:
            case DATE_TIME:
            case TIMESTAMP:
            case BOOL:
                return HorizontalAlignment.CENTER;
            case VCHAR:

            case ADDRESS:
        }
        return HorizontalAlignment.LEFT;
    }

    protected String read(ResultSet resultSet, ColumnInfo column) throws SQLException {
        final ColumnType type = column.getType();
        switch (type) {
            case KEY:
            case TEMPORAL_KEY:
            case IDENTIFIER:
                final long longValue = resultSet.getLong(column.getAlias());
                if (resultSet.wasNull()) {
                    return Strings.EMPTY;
                }
                return String.valueOf(longValue);
            case CURRENCY:
                final BigDecimal decimalValue = resultSet.getBigDecimal(column.getAlias());
                if (decimalValue == null) {
                    return Strings.EMPTY;
                }
                return String.valueOf(decimalValue);
            case INTEGER:
                final long value = resultSet.getLong(column.getAlias());
                if (resultSet.wasNull()) {
                    return Strings.EMPTY;
                }
                final String format = column.getFormat();
                if (format != null) {
                    final Renderer integerRenderer = rendererManager.getRenderer(format);
                    if (integerRenderer != null) {
                        return i18nResource.i18n(integerRenderer.render((int) value));
                    }
                }
                return String.valueOf(value);
            case DATE:
            case YMDAY:
            case DATE_TIME:
                final Date dateValue = resultSet.getDate(column.getAlias());
                return dateValue == null ? null : getDateFormat(getFormat(column)).format(dateValue);
            case TIMESTAMP:
                final Timestamp timestampValue = resultSet.getTimestamp(column.getAlias());
                return timestampValue == null ? null : getDateFormat(getFormat(column)).format(timestampValue);
            case BOOL:
                Renderer renderer = rendererManager.getRenderer(column.getFormat());
                if (renderer == null) {
                    renderer = rendererManager.getRenderer(Formats.BOOL_DEFAULT);
                }

                return i18nResource.i18n(renderer.render(resultSet.getBoolean(column.getAlias())));
            //return i18nResource.i18n(renderer.render(resultSet.getInt(column.getAlias()) != 0));
            case VCHAR:

                if (column.getFormat() != null) {
                    Renderer vcharrenderer = rendererManager.getRenderer(column.getFormat());
                    if (vcharrenderer != null) {
                        return i18nResource.i18n(vcharrenderer.render(resultSet.getString(column.getAlias())));

                    }
                }
                return resultSet.getString(column.getAlias());
            case ADDRESS:
                return column.read(resultSet).toString();
        }
        return null;
    }

    public DateFormat getDateFormat(String pattern) {
        DateFormat result = getCache().get(pattern);
        if (result == null) {
            result = new SimpleDateFormat(pattern);
            getCache().put(pattern, result);
        }
        return result;
    }

    public HashMap<String, DateFormat> getCache() {
        if (cache == null) {
            cache = new HashMap<>();
        }
        return cache;
    }

}
