package ru.sbsoft.client.schedule;

/**
 * Синхронная команда. Запускает очередную итерацию менеджера сразу после выполнения своей функции.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public abstract class SyncSchedulerChainCommand extends SchedulerChainCommand {

    @Override
    public final void execute() {
        onCommand();
        getChainManager().next();
    }

    /**
     * запусается для выполнения основной логики.
     */
    protected abstract void onCommand();
}
