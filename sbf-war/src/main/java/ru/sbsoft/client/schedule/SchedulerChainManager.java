package ru.sbsoft.client.schedule;

import com.google.gwt.user.client.Timer;
import java.util.List;

/**
 * Менеджер последовательных операций, ктороые могут быть как синхронными, так и
 * асинхронными. Поочередно запускает операции списка. Операция оповещает
 * менеджер об окончании своей работы через методы next() или repeat().
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public final class SchedulerChainManager {

    int i = 0;
    List<SchedulerChainCommand> commandChain;

    public SchedulerChainManager(List<SchedulerChainCommand> commandChain) {
        this.commandChain = commandChain;
    }

    public void next() {
        if (commandChain.size() > i) {
            final SchedulerChainCommand command = commandChain.get(i++);
            execute(command);
        }
    }

    public void repeat() {
        if (commandChain.size() >= i) {
            final SchedulerChainCommand command = commandChain.get(i - 1);
            execute(command);
        }
    }

    private void execute(final SchedulerChainCommand command) {
        command.setManager(this);
        new Timer() {
            @Override
            public void run() {
                command.execute();
            }
        }.schedule(30);
    }

}
