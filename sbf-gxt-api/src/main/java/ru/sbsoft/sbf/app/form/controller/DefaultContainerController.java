package ru.sbsoft.sbf.app.form.controller;

import ru.sbsoft.sbf.app.form.DefaultHandlerHolder;
import ru.sbsoft.sbf.app.form.IHandler;
import java.util.*;
import ru.sbsoft.sbf.app.ICondition;
import ru.sbsoft.sbf.app.Registration;
import ru.sbsoft.sbf.app.form.*;
import ru.sbsoft.sbf.app.form.IHasChangeValueHandlers.ChangeValueEvent;
import ru.sbsoft.sbf.app.form.controller.IFormContainerController.AfterSetValueEvent;
import ru.sbsoft.sbf.app.form.controller.IFormContainerController.BeforeSetValueEvent;
import static ru.sbsoft.sbf.app.utils.CollectionUtils.wrapList;
import ru.sbsoft.sbf.app.utils.RegistrationUtils;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL>
 */
public class DefaultContainerController<MODEL> implements IFormContainerController<MODEL> {

    private List<IFormElementController<MODEL>> controllers;

    private final DefaultHandlerHolder<BeforeSetValueEvent<MODEL>> beforeSetValueHandlers = new DefaultHandlerHolder<BeforeSetValueEvent<MODEL>>();
    private final DefaultHandlerHolder<AfterSetValueEvent<MODEL>> afterSetValueHandlers = new DefaultHandlerHolder<AfterSetValueEvent<MODEL>>();
    private final DefaultHandlerHolder<ChangeValueEvent<MODEL>> valueChangeHandlers = new DefaultHandlerHolder<ChangeValueEvent<MODEL>>();

    private List<ICondition> readOnlyConditions;
    private boolean readOnly = false;
    private MODEL model;

    private IFormElement element;

    private boolean modelNeedsToRead = false;

    private final IHandler<ChangeValueEvent> changeHandler = new IHandler<ChangeValueEvent>() {
        @Override
        public void onHandle(ChangeValueEvent event) {
            modelNeedsToRead = true;
            onValueChange(event);
        }
    };

    private Map<IFormElementController, Registration> fieldChangeHandlers = new HashMap<IFormElementController, Registration>();

    //<editor-fold defaultstate="collapsed" desc="Handlers">
    @Override
    public Registration addBeforeSetValueHandler(IHandler<BeforeSetValueEvent<MODEL>> handler) {
        return beforeSetValueHandlers.addHandler(handler);
    }

    private void onBeforeSetValue(MODEL oldModel, MODEL newModel) {
        final BeforeSetValueEvent event = new BeforeSetValueEvent();
        event.setModel(newModel);
        event.setOldModel(oldModel);
        event.setOwner(this);
        beforeSetValueHandlers.onHandler(event);
    }

    @Override
    public Registration addAfterSetValueHandler(IHandler<AfterSetValueEvent<MODEL>> handler) {
        return afterSetValueHandlers.addHandler(handler);
    }

    private void onAfterSetValue(MODEL model) {
        final AfterSetValueEvent event = new AfterSetValueEvent();
        event.setModel(model);
        event.setOwner(this);
        afterSetValueHandlers.onHandler(event);
    }

    public List<ICondition> getReadOnlyConditions() {
        if (readOnlyConditions == null) {
            readOnlyConditions = new ArrayList<ICondition>();
        }
        return readOnlyConditions;
    }

    @Override
    public Registration addReadOnlyCondition(ICondition readOnlyCondition) {
        return RegistrationUtils.register(getReadOnlyConditions(), readOnlyCondition);
    }

    @Override
    public Registration addChangeValueHandler(IHandler<ChangeValueEvent<MODEL>> handler) {
        return valueChangeHandlers.addHandler(handler);
    }

    private void onValueChange(ChangeValueEvent event) {
        ChangeValueEvent formChangedEvent = new ChangeValueEvent();
        formChangedEvent.setValue(getValue());
        formChangedEvent.setOwner(this);
        valueChangeHandlers.onHandler(formChangedEvent);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Form Element Controllers">
    public List<IFormElementController<MODEL>> getFormElementControllers() {
        if (controllers == null) {
            controllers = new ArrayList<IFormElementController<MODEL>>();
        }
        return controllers;
    }

    @Override
    public void addFormElementController(IFormElementController<MODEL> formElementController) {
        getFormElementControllers().add(formElementController);
        if (formElementController instanceof IHasChangeValueHandlers) {
            fieldChangeHandlers.put(formElementController, ((IHasChangeValueHandlers) formElementController).addChangeValueHandler(changeHandler));
        }
        formElementController.setReadOnly(readOnly);
        formElementController.resetValidationErrors();
        formElementController.writeFrom(model, true);
    }

    @Override
    public void removeFormElementController(final IFormElementController<MODEL> formElementController) {
        if (controllers == null) {
            return;
        }
        for (final IFormElementController e : wrapList(getFormElementControllers())) {
            if (e.equals(formElementController)) {
                final Registration registration = fieldChangeHandlers.get(e);
                if (registration != null) {
                    registration.remove();
                }
                getFormElementControllers().remove(e);
                return;
            }
        }
    }

    @Override
    public void resetValidationErrors() {
        if (controllers == null) {
            return;
        }
        for (IFormElementController formElement : wrapList(getFormElementControllers())) {
            formElement.resetValidationErrors();
        }
    }

    private void writeFromModel(final MODEL model) {
        if (controllers == null) {
            return;
        }
        for (IFormElementController formElement : wrapList(getFormElementControllers())) {
            formElement.writeFrom(model, true);
        }
    }
    //</editor-fold>

    @Override
    public void setReadOnly(boolean readOnly) {
        boolean changed = readOnly != this.readOnly;
        this.readOnly = readOnly;
        if (changed) {
            updateReadonly(readOnly);
        }
    }

    @Override
    public boolean isReadOnly() {
        if (readOnly) {
            return true;
        }
        for (ICondition condition : wrapList(getReadOnlyConditions())) {
            if (condition.check()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean wasChanged() {
        return modelNeedsToRead;
    }

    @Override
    public void writeFrom(MODEL model, boolean fireEvents) {
        final boolean readonlyStat = isReadOnly();

        onBeforeSetValue(this.model, model);
        this.model = model;
        resetValidationErrors();
        writeFromModel(model);
        if (fireEvents) {
            onAfterSetValue(model);
        }

        final boolean readonlyNewStat = isReadOnly();
        if (readonlyStat != readonlyNewStat) {
            updateReadonly(readonlyNewStat);
        }
    }

    @Override
    public void readTo(MODEL model) {
        if (controllers == null) {
            return;
        }
        for (IFormElementController formElement : wrapList(getFormElementControllers())) {
            formElement.readTo(model);
        }
    }

    public MODEL getValue() {
        if (modelNeedsToRead) {
            readTo(model);
            modelNeedsToRead = false;
        }
        return model;
    }

    private void updateReadonly(boolean readonly) {
        if (controllers == null) {
            return;
        }
        for (IFormElementController formElement : wrapList(getFormElementControllers())) {
            formElement.setReadOnly(readonly);
        }
    }

    @Override
    public void validate() {
        if (controllers == null) {
            return;
        }
        for (IFormElementController formElement : wrapList(getFormElementControllers())) {
            formElement.validate();
        }
    }

    @Override
    public boolean isValid() {
        if (controllers == null) {
            return true;
        }
        for (IFormElementController formElement : wrapList(getFormElementControllers())) {
            if (!formElement.isValid()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFormElement(IFormElement formElement) {
        element = formElement;
    }

    @Override
    public IFormElement getFormElement() {
        return element;
    }

}
