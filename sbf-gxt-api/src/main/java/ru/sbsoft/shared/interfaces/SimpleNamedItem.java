package ru.sbsoft.shared.interfaces;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 *
 * @author Kiselev
 */
public class SimpleNamedItem implements NamedItem {
    private String code;
    private ILocalizedString itemName;
    
    private SimpleNamedItem(){
    }

    public SimpleNamedItem(String code, String itemName) {
        this(code, new NonLocalizedString(itemName));
    }
    
    public SimpleNamedItem(String code, ILocalizedString itemName) {
        this.itemName = itemName;
        this.code = code;
    }
    
    

    @Override
    public ILocalizedString getItemName() {
        return itemName;
    }

    @Override
    public String getCode() {
        return code;
    }
    
}
