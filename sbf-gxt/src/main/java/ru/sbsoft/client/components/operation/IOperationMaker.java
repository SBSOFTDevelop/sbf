package ru.sbsoft.client.components.operation;

/**
 * Создает объект операции.
 * Конкретное поведение определяется потомками.
 * Основное использование в {@link BaseOperationManager} при запуске автономных операций
 * и в {@link ru.sbsoft.client.components.browser.actions.GridOperationAction} при запуске табличных операций.
 */
public interface IOperationMaker {

    String getOperationCode();

    AbstractOperation createOperation();
}
