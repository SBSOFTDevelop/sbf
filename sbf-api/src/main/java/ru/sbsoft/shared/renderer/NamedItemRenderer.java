package ru.sbsoft.shared.renderer;

import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

/**
 *
 * @author Kiselev
 */
public class NamedItemRenderer extends Renderer<String> {

    public NamedItemRenderer(NamedItem... items) {
        for(NamedItem item : items){
            addItem(item.getCode(), item.getItemName());
        }
    }
    
    @Override
    public ILocalizedString render(String value) {
        return render(value, new NonLocalizedString(value));
    }
}
