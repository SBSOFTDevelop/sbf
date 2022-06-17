package ru.sbsoft.sbf.app.form.controller;

import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.sbf.app.form.DefaultEvent;
import ru.sbsoft.sbf.app.form.IHandler;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL>
 */
public interface IFormSaveController<MODEL> {

    void save(MODEL model, FormSaveBehvior saveBehavior);

    Registration addBeforeSaveHandler(IHandler<BeforeSaveEvent<MODEL>> handler);

    Registration addSaveHandler(IHandler<SaveEvent<MODEL>> handler);

    Registration addSaveErrorHandler(IHandler<SaveErrorEvent<MODEL>> handler);

    Registration addPersistHandler(IHandler<PersistEvent<MODEL>> handler);

    Registration addEditHandler(IHandler<EditEvent<MODEL>> handler);

    interface FormSaveCallback<MODEL> {

        void onResult(MODEL model);

        /**
         *
         * @param error ошибка, возникшая при попытке сохранения.
         * @return true, если ошибка обработана, иначе false
         */
        boolean onFailure(Throwable error);
    }

    interface FormSaveBehvior<MODEL> {

        void save(MODEL model, FormSaveCallback<MODEL> saveCallback);
    }

    abstract class ModelEvent<MODEL> extends DefaultEvent<IFormSaveController> {

        private MODEL model;

        public MODEL getModel() {
            return model;
        }

        public void setModel(MODEL model) {
            this.model = model;
        }
    }

    class SaveEvent<MODEL> extends ModelEvent<MODEL> {
    }

    class SaveErrorEvent<MODEL> extends ModelEvent<MODEL> {

        private Throwable caught;
        private boolean handled;

        public void setCaught(Throwable caught) {
            this.caught = caught;
        }

        public Throwable getCaught() {
            return caught;
        }

        public boolean isHandled() {
            return handled;
        }

        public void setHandled(boolean handled) {
            this.handled = handled;
        }

    }

    class BeforeSaveEvent<MODEL> extends ModelEvent<MODEL> {
    }

    class PersistEvent<MODEL> extends ModelEvent<MODEL> {
    }

    class EditEvent<MODEL> extends ModelEvent<MODEL> {
    }
}
