package ru.sbsoft.client.components.actions.event;

import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.client.components.actions.ButtonIconSize;

/**
 *
 * @author vlki
 */
public class IconChangeEvent extends ActionValueChangeEvent<IconChangeHandler, ImageResource> {

    public static final Type<IconChangeHandler> TYPE = new Type<IconChangeHandler>();
    
    private final ButtonIconSize size;

    public IconChangeEvent(ImageResource ico, ButtonIconSize size) {
        super(TYPE, ico);
        this.size = size;
    }

    public ButtonIconSize getSize() {
        return size;
    }
}
