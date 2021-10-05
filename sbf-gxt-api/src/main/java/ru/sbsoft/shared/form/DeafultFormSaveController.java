package ru.sbsoft.shared.form;

import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.sbf.app.form.DefaultHandlerHolder;
import ru.sbsoft.sbf.app.form.IHandler;
import ru.sbsoft.sbf.app.form.controller.IFormSaveController;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL>
 */
public class DeafultFormSaveController<MODEL extends IFormModel> implements IFormSaveController<MODEL> {

    private final DefaultHandlerHolder<PersistEvent<MODEL>> persistHandlers = new DefaultHandlerHolder<>();
    private final DefaultHandlerHolder<BeforeSaveEvent<MODEL>> beforeSaveHandlers = new DefaultHandlerHolder<>();
    private final DefaultHandlerHolder<SaveEvent<MODEL>> saveHandlers = new DefaultHandlerHolder<>();
    private final DefaultHandlerHolder<SaveErrorEvent<MODEL>> saveErrorHandlers = new DefaultHandlerHolder<SaveErrorEvent<MODEL>>() {
        @Override
        public void onHandle(IHandler<SaveErrorEvent<MODEL>> handler, SaveErrorEvent<MODEL> event) {
            if (!event.isHandled()) {
                super.onHandle(handler, event);
            }
        }
    };
    private final DefaultHandlerHolder<EditEvent<MODEL>> editHandlers = new DefaultHandlerHolder<>();

    @Override
    public Registration addBeforeSaveHandler(IHandler<BeforeSaveEvent<MODEL>> handler) {
        return beforeSaveHandlers.addHandler(handler);
    }

    private void onBeforeSave(MODEL model) {
        final BeforeSaveEvent<MODEL> event = new BeforeSaveEvent<>();
        event.setModel(model);
        event.setOwner(this);
        beforeSaveHandlers.onHandler(event);
    }

    @Override
    public Registration addSaveHandler(IHandler<SaveEvent<MODEL>> handler) {
        return saveHandlers.addHandler(handler);
    }

    private void onSave(MODEL model) {
        final SaveEvent event = new SaveEvent();
        event.setModel(model);
        event.setOwner(this);
        saveHandlers.onHandler(event);
    }

    @Override
    public Registration addSaveErrorHandler(IHandler<SaveErrorEvent<MODEL>> handler) {
        return saveErrorHandlers.addHandler(handler);
    }

    /**
     * @param model model was tried to save
     * @param caught error on save
     * @return true if error was handler, else false
     */
    private boolean onSaveError(MODEL model, Throwable caught) {
        final SaveErrorEvent event = new SaveErrorEvent();
        event.setCaught(caught);
        event.setOwner(this);
        event.setModel(model);
        saveErrorHandlers.onHandler(event);
        return event.isHandled();
    }

    @Override
    public Registration addPersistHandler(IHandler<PersistEvent<MODEL>> handler) {
        return persistHandlers.addHandler(handler);
    }

    private void onPersist(MODEL model) {
        final PersistEvent event = new PersistEvent();
        event.setModel(model);
        event.setOwner(this);
        persistHandlers.onHandler(event);
    }

    @Override
    public Registration addEditHandler(IHandler<EditEvent<MODEL>> handler) {
        return editHandlers.addHandler(handler);
    }

    private void onEdit(MODEL model) {
        final EditEvent event = new EditEvent();
        event.setModel(model);
        event.setOwner(this);
        editHandlers.onHandler(event);
    }

    @Override
    public void save(final MODEL model, final FormSaveBehvior saveBehavior) {
        onBeforeSave(model);
        final FormSaveCallback<MODEL> saveCallback = new FormSaveCallback<MODEL>() {
            @Override
            public void onResult(MODEL result) {
                if (result != null && result.getId() != null) {
                    if (model == null || model.getId() == null) {
                        onPersist(result);
                    } else {
                        onEdit(result);
                    }
                    onSave(result);
                } else {
                    onFailure(new IllegalArgumentException("Illegal save result: " + result + " [ID: " + (result != null ? result.getId() : null) + "]"));
                }
            }

            @Override
            public boolean onFailure(Throwable caught) {
                return onSaveError(model, caught);
            }

        };
        saveBehavior.save(model, saveCallback);
    }
}
