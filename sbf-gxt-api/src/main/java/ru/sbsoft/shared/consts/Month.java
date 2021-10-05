package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.interfaces.NumeratedItem;

/**
 *
 * @author Kiselev
 */
public enum Month implements NumeratedItem {

    JAN(1, SBFGeneralStr.rendJanuary, SBFGeneralStr.rendGenJanuary),
    FEB(2, SBFGeneralStr.rendFebruary, SBFGeneralStr.rendGenFebruary),
    MAR(3, SBFGeneralStr.rendMarch, SBFGeneralStr.rendGenMarch),
    APR(4, SBFGeneralStr.rendApril, SBFGeneralStr.rendGenApril),
    MAY(5, SBFGeneralStr.rendMay, SBFGeneralStr.rendGenMay),
    JUN(6, SBFGeneralStr.rendJune, SBFGeneralStr.rendGenJune),
    JUL(7, SBFGeneralStr.rendJuly, SBFGeneralStr.rendGenJuly),
    AUG(8, SBFGeneralStr.rendAugust, SBFGeneralStr.rendGenAugust),
    SEP(9, SBFGeneralStr.rendSeptember, SBFGeneralStr.rendGenSeptember),
    OCT(10, SBFGeneralStr.rendOctober, SBFGeneralStr.rendGenOctober),
    NOV(11, SBFGeneralStr.rendNovember, SBFGeneralStr.rendGenNovember),
    DEC(12, SBFGeneralStr.rendDecember, SBFGeneralStr.rendGenDecember);

    public static final int MIN_MONTH_NUM = 1;
    public static final int MAX_MONTH_NUM = 12;

    private final int num;
    private final ILocalizedString itemName;
    private final ILocalizedString genitiveName;

    private Month(int num, I18nResourceInfo resourceInfo, I18nResourceInfo genResourceInfo) {
        checkMonthNum(num);
        this.num = num;
        this.itemName = new LocalizedString(resourceInfo);
        this.genitiveName = new LocalizedString(genResourceInfo);
    }

    @Override
    public int getNum() {
        return num;
    }
    
    public String getNumStr(){
        return getNumStr(num);
    }
    
    public static String getNumStr(int num){
        checkMonthNum(num);
        return num < 10 ? "0" + Integer.toString(num) : Integer.toString(num);
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }


    public ILocalizedString getGenitiveName() {
        return genitiveName;
    }

    @Override
    public String getCode() {
        return name();
    }

    public int getQuarterNum() {
        return getQuarterNum(num);
    }

    public static int getQuarterNum(int monthNum) {
        checkMonthNum(monthNum);
        return (int) ((monthNum - 1) / 3) + 1;
    }

    public static Month getByNum(int num) {
        checkMonthNum(num);
        for (Month m : values()) {
            if (m.getNum() == num) {
                return m;
            }
        }
        return null; // impossible
    }

    public static NamedItem getNameByNum(int num) {
        return getByNum(num);
    }

    public static int getMinNum() {
        return MIN_MONTH_NUM;
    }

    public static int getMaxNum() {
        return MAX_MONTH_NUM;
    }
    
    public static void checkMonthNum(int num){
        if (num < MIN_MONTH_NUM || num > MAX_MONTH_NUM) {
            throw new ApplicationException(SBFExceptionStr.monthRange, Integer.toString(MIN_MONTH_NUM), Integer.toString(MAX_MONTH_NUM));
        }
    }
}
