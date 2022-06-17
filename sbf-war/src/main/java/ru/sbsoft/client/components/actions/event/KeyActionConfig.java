package ru.sbsoft.client.components.actions.event;

/**
 *
 * @author vk
 */
public class KeyActionConfig {

    private final KeyAction action;
    private boolean preventDefault = true;
    private boolean stopPropagation = true;

    public KeyActionConfig(KeyAction action) {
        this.action = action;
    }

    public KeyAction getAction() {
        return action;
    }

    public boolean isPreventDefault() {
        return preventDefault;
    }

    public KeyActionConfig setPreventDefault(boolean preventDefault) {
        this.preventDefault = preventDefault;
        return this;
    }

    public boolean isStopPropagation() {
        return stopPropagation;
    }

    public KeyActionConfig setStopPropagation(boolean stopPropagation) {
        this.stopPropagation = stopPropagation;
        return this;
    }
}
