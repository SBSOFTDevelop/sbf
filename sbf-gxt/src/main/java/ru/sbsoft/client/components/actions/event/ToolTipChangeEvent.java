package ru.sbsoft.client.components.actions.event;

/**
 *
 * @author vlki
 */
public class ToolTipChangeEvent extends ActionValueChangeEvent<ToolTipChangeHandler, String> {

    public static final Type<ToolTipChangeHandler> TYPE = new Type<ToolTipChangeHandler>();

    public ToolTipChangeEvent(String text) {
        super(TYPE, text);
    }
}
