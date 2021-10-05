package ru.sbsoft.client.components.grid;

import java.util.Map;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.renderer.Renderer;

public class KeyNameRenderer extends Renderer {

    public KeyNameRenderer(Map<?, ILocalizedString> items) {
        for (Map.Entry<?, ILocalizedString> item : items.entrySet()) {
            addItem(item.getKey(), item.getValue());
        }
    }

    @Override
    public ILocalizedString render(Object value) {
        ILocalizedString def = value != null ? new NonLocalizedString(value.toString()) : NonLocalizedString.EMPTY;
        return render(value, def);
    }
}
