package ru.sbsoft.shared.renderer;

import java.util.EnumSet;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;

public class EnumRenderer<KEY_TYPE, E extends Enum<E> & RenderableEnum<KEY_TYPE>> extends Renderer<KEY_TYPE> {

    public EnumRenderer(Class<E> eClass) {
        this(eClass, null);
    }

    private EnumRenderer(Class<E> eClass, String allItemCaption) {
        if (allItemCaption != null) {
            addItem(null, allItemCaption);
        }
        for (E e : EnumSet.allOf(eClass)) {
            addItem(e);
        }
    }

    public EnumRenderer(E... values) {
        for (E e : values) {
            addItem(e);
        }
    }

    private void addItem(E e) {
        addItem(e.getKey(), e.getRenderString(false), e.getRenderString(true));
    }

    @Override
    public ILocalizedString render(KEY_TYPE value) {
        ILocalizedString res = super.render(value);
        return res != null ? res : new NonLocalizedString(String.valueOf(value));
    }
}
