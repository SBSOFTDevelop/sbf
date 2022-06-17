package ru.sbsoft.shared.renderer;

import ru.sbsoft.shared.api.i18n.ILocalizedString;


public class LongRenderer extends Renderer<Long> {

    public LongRenderer(ILocalizedString... items) {
        if (null != items) {
            for (int i = 0; i < items.length; i++) {
                addItem(Long.valueOf(i), items[i]);
            }
        }
    }
}
