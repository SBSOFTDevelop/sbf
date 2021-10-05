package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL>
 */
public interface IWritable<MODEL> {

    /**
     * Заполняет значение элемента в соответствии с переданной моделью. Модель
     * может быть null, такой случай следует рассматривать как пустое значение.
     *
     * @param model модель для заполнения.
     */
    public void writeFrom(MODEL model, boolean fireEvents);
}
