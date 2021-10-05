package ru.sbsoft.client.schedule;

import com.google.gwt.core.client.Scheduler.ScheduledCommand;

/**
 * Команда, выполняемая в порядке последовательного выполнения. Когда выполнение команды
 * оканчивается, она модет вызывать очередную итерацию менеджера.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public abstract class SchedulerChainCommand implements ScheduledCommand {

    private SchedulerChainManager chainManager;

    public void setManager(SchedulerChainManager chainManager) {
        this.chainManager = chainManager;
    }

    public SchedulerChainManager getChainManager() {
        return chainManager;
    }

}
