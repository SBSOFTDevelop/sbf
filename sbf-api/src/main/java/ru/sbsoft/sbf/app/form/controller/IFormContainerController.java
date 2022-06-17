package ru.sbsoft.sbf.app.form.controller;

import ru.sbsoft.sbf.app.ICondition;
import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.sbf.app.form.*;

/**
 * Описывает основное поведение формы в части работы с данными. Все обработчики
 * событий отрабатывают синхронно.
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL> модель формы
 */
public interface IFormContainerController<MODEL>
        extends
        IHasChangeValueHandlers<MODEL>,
        IHasFormElementControllers<MODEL>,
        IFormElementController<MODEL> {

    /**
     * Добавляет обработчик события перед установкой данных формы.
     *
     * @param handler обработчик события.
     * @return ссылку на удаление обработчика.
     */
    Registration addBeforeSetValueHandler(IHandler<BeforeSetValueEvent<MODEL>> handler);

    /**
     * Добавляет обработчик события после установки данных формы.
     *
     * @param handler обработчик события.
     * @return ссылку на удаление обработчика.
     */
    Registration addAfterSetValueHandler(IHandler<AfterSetValueEvent<MODEL>> handler);

    /**
     * Добавляет условие для проверки режима read only.
     *
     * @param readOnlyCondition обработчик события.
     * @return ссылку на удаление обработчика.
     */
    Registration addReadOnlyCondition(ICondition readOnlyCondition);

    /**
     * Возвращает текущий статус данных.
     *
     * @return true, если данные в форме были изменениы с момента последнего
     * вызова setModel() или save().
     */
    boolean wasChanged();


    class FormEvent extends DefaultEvent<IFormContainerController> {

        private Object sourceEvent;

        public Object getSourceEvent() {
            return sourceEvent;
        }

        public void setSourceEvent(Object sourceEvent) {
            this.sourceEvent = sourceEvent;
        }
    }

    class FormModelEvent<MODEL> extends FormEvent {

        private MODEL model;

        public MODEL getModel() {
            return model;
        }

        public void setModel(MODEL model) {
            this.model = model;
        }
    }

    class BeforeSetValueEvent<MODEL> extends FormModelEvent<MODEL> {

        private MODEL oldModel;

        public MODEL getOldModel() {
            return oldModel;
        }

        public void setOldModel(MODEL oldModel) {
            this.oldModel = oldModel;
        }

    }

    class AfterSetValueEvent<MODEL> extends FormModelEvent<MODEL> {
    }

}
