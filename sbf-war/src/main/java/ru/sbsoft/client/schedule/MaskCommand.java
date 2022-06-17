package ru.sbsoft.client.schedule;

import ru.sbsoft.svc.widget.core.client.Component;

/**
 * Накладывает маску (блокирующий текст) на указанную компоненту SVC.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class MaskCommand extends SyncSchedulerChainCommand {

    private final String text;
    private final Component component;

    public MaskCommand(Component component, String text) {
        this.component = component;
        this.text = text;
    }

    @Override
    protected void onCommand() {
        component.mask(text);
    }

}
