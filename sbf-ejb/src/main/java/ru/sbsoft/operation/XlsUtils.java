package ru.sbsoft.operation;

import org.apache.poi.hssf.record.cf.BorderFormatting;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.model.ThemesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;

public final class XlsUtils {


    private static CTBorder getCTBorder(StylesTable stylesTable, CTXf cellXf) {
        CTBorder ct;
        if (cellXf.getApplyBorder()) {
            int idx = (int) cellXf.getBorderId();
            XSSFCellBorder cf = stylesTable.getBorderAt(idx);
            ct = (CTBorder) cf.getCTBorder().copy();
        } else {
            ct = CTBorder.Factory.newInstance();
        }
        return ct;
    }

    private static void initBorderedStyle(CellStyle style) {
        short black = IndexedColors.BLACK.getIndex();
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(black);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(black);
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(black);
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(black);
        style.setLocked(true);

    }

    public static void protectSheet(Sheet sheet, String passwd) {


        if (sheet instanceof XSSFSheet) {

            XSSFSheet s = (XSSFSheet) sheet;

            s.lockDeleteColumns(true);
            s.lockFormatColumns(false);
            s.lockFormatRows(false);

            s.enableLocking();
            ((XSSFWorkbook) sheet.getWorkbook()).lockStructure();
            s.enableLocking();
        }
        sheet.protectSheet(passwd);
    }


    public static CellStyle getDiagonalBorder(Workbook wb) {
        CellStyle style = wb.createCellStyle();

        initBorderedStyle(style);

        StylesTable stylesSource = null;
        if (wb instanceof SXSSFWorkbook) {
            stylesSource = ((SXSSFWorkbook) wb).getXSSFWorkbook().getStylesSource();
        } else if (wb instanceof XSSFWorkbook) {
            stylesSource = ((XSSFWorkbook) wb).getStylesSource();
        }
        //if (stylesSource == null) return;

        ThemesTable theme = stylesSource.getTheme();
        CTXf cellXf = ((XSSFCellStyle) style).getCoreXf();

        CTBorder ct = getCTBorder(stylesSource, cellXf);
        CTBorderPr pr = ct.isSetDiagonal() ? ct.getDiagonal() : ct.addNewDiagonal();
        ct.setDiagonalDown(true);
        ct.setDiagonalUp(true);
        pr.setStyle(STBorderStyle.Enum.forInt(BorderFormatting.BORDER_THIN + 1));
        int idx = stylesSource.putBorder(new XSSFCellBorder(ct, theme, stylesSource.getIndexedColors()));
        cellXf.setBorderId(idx);
        cellXf.setApplyBorder(true);
        style.setLocked(true);
        return style;


    }


}

