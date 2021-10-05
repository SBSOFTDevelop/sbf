package ru.sbsoft.shared.consts;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author sokolov
 */
public enum CustomReportType implements NamedItem {
    
    DOCX("DOCX"),
    RTF("RTF"),
    XLSX("XLSX"),
    XLS("XLS"),
    ODT("ODT"),
    ODS("ODS"),
    HTML("HTML"),
    CSV("CSV"),
    JPRINT("JPIINT"),
    PDF("PDF")
    ;
    
    private final ILocalizedString itemName;

    private CustomReportType(String itemName) {
        this.itemName = new NonLocalizedString(itemName);
    }

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

    @Override
    public String getCode() {
       return name();
    }
        
}
