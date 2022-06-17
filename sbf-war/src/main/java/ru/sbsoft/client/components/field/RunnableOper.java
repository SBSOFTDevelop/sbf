package ru.sbsoft.client.components.field;

import ru.sbsoft.shared.interfaces.OperationType;

/**
 *
 * @author sokolov
 */
public interface RunnableOper extends Runnable {
    
    OperationType getOperType();
}
