package ru.sbsoft.client.components.grid;

import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Kiselev
 */
public interface ILastUserActionRunner<DataModel extends MarkModel> {

    boolean trySetAction(UserAction lastAction);

    boolean trySetAction(UserAction lastAction, DataModel lastModel);

    void setAction(UserAction lastAction);

    void setAction(UserAction lastAction, DataModel lastModel);
}
