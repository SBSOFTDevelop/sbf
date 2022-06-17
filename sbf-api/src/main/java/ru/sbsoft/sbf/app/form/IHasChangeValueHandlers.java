package ru.sbsoft.sbf.app.form;

import ru.sbsoft.sbf.app.Registration;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <VALUE>
 */
public interface IHasChangeValueHandlers<VALUE> {

    /**
     * Добавляет обработчик события изменения значения.
     *
     * @param handler обработчик
     * @return ссылку на удаление обработчика.
     */
    Registration addChangeValueHandler(IHandler<ChangeValueEvent<VALUE>> handler);

    class ChangeValueEvent<VALUE> extends DefaultEvent<IHasValue<VALUE>> {

        private VALUE value;

        public VALUE getValue() {
            return value;
        }

        public void setValue(VALUE value) {
            this.value = value;
        }

    }

}
