package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL>
 */
public interface IReadable<MODEL> {

    /**
     * Заполняет модель текущим значением из элемента.
     *
     * @param model модель для заполнения.
     */
    public void readTo(MODEL model);
}
