package ru.sbsoft.shared.renderer;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.api.i18n.LocalizedString;

/**
 * @author balandin
 * @since May 12, 2014 5:14:01 PM
 */
public class MonthRenderer extends Renderer<Long> {

    public MonthRenderer() {
        super();
        init();
    }

    private void init() {
        ILocalizedString[] tmp = new ILocalizedString[]{
            new LocalizedString(SBFGeneralStr.rendJanuary),
            new LocalizedString(SBFGeneralStr.rendFebruary),
            new LocalizedString(SBFGeneralStr.rendMarch),
            new LocalizedString(SBFGeneralStr.rendApril),
            new LocalizedString(SBFGeneralStr.rendMay),
            new LocalizedString(SBFGeneralStr.rendJune),
            new LocalizedString(SBFGeneralStr.rendJuly),
            new LocalizedString(SBFGeneralStr.rendAugust),
            new LocalizedString(SBFGeneralStr.rendSeptember),
            new LocalizedString(SBFGeneralStr.rendOctober),
            new LocalizedString(SBFGeneralStr.rendNovember),
            new LocalizedString(SBFGeneralStr.rendDecember)
        };
        for (int i = 0;
                i < tmp.length;
                i++) {
            addItem((long) (i + 1), tmp[i]);
        }
    }
}
