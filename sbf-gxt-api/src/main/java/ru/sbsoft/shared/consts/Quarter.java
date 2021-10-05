package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.interfaces.NumeratedItem;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 *
 * @author Kiselev
 */
public enum Quarter implements NumeratedItem {

    FIRST(1, "I"),
    SECOND(2, "II"),
    THIRD(3, "III"),
    FOURTH(4, "IV");

    public static final int MIN_QUARTER_NUM = 1;
    public static final int MAX_QUARTER_NUM = 4;
    
    private final int num;
    private final ILocalizedString itemName;

    private Quarter(int num, String itemName) {
        if (num < MIN_QUARTER_NUM || num > MAX_QUARTER_NUM) {
            throw new ApplicationException(SBFExceptionStr.quarterRange, String.valueOf(MIN_QUARTER_NUM), String.valueOf(MAX_QUARTER_NUM));
        }
        this.num = num;
        this.itemName = new NonLocalizedString(itemName);
    }

    @Override
    public int getNum() {
        return num;
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

    @Override
    public String getCode() {
        return name();
    }
    
    public int getMonthNum(){
        return getMonthNum(num);
    }
    
    public static int getMonthNum(int quarterNum){
        return (quarterNum - 1) * 3 + 1;
    }

    public static Quarter getByNum(int num) {
        if (num < 1) {
            throw new ApplicationException(SBFExceptionStr.quarterLess, String.valueOf(num));
        }
        if (num > 4) {
            throw new ApplicationException(SBFExceptionStr.quarterMore, String.valueOf(num));
        }
        for (Quarter m : values()) {
            if (m.getNum() == num) {
                return m;
            }
        }
        return null; // impossible
    }

    public static ILocalizedString getNameByNum(int num) {
        return getByNum(num).itemName;
    }

    public static int getMinNum() {
        return MIN_QUARTER_NUM;
    }

    public static int getMaxNum() {
        return MAX_QUARTER_NUM;
    }

}
