package ru.sbsoft.client.components.grid;

import com.google.gwt.core.client.Scheduler;

/**
 *
 * @author Kiselev
 */
public class StateUpdater {

    private final Scheduler.ScheduledCommand command;
    private boolean sheduled = false;
    private final CommandExecutor executor;

    public StateUpdater(Scheduler.ScheduledCommand updateCommand) {
        this.command = updateCommand;
        executor = new CommandExecutor();
    }

    public void requestUpdate() {
        if (!sheduled) {
            sheduled = true;
            Scheduler.get().scheduleFinally(executor);
        }
    }

    private class CommandExecutor implements Scheduler.ScheduledCommand {
        @Override
        public void execute() {
            command.execute();
            sheduled = false;
        }
    }
}
