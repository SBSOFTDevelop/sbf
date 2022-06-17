package ru.sbsoft.client.schedule;

import ru.sbsoft.svc.widget.core.client.Component;

/**
 * Снимает маску (блокирующий текст) с компоненты SVC.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class UnmaskCommand extends SyncSchedulerChainCommand {

    private final Component viewport;

    public UnmaskCommand(Component viewport) {
        this.viewport = viewport;
    }

    @Override
    public void onCommand() {
        viewport.unmask();
    }
}
