package ru.sbsoft.client.components.actions.event;

/**
 *
 * @author vlki
 */
public class TextChangeEvent extends ActionValueChangeEvent<TextChangeHandler, String> {

    public static final Type<TextChangeHandler> TYPE = new Type<TextChangeHandler>();

    public TextChangeEvent(String text) {
        super(TYPE, text);
    }
}
