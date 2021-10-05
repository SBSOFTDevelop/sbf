package ru.sbsoft.client.components.form;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.form.action.UpdateEditorsStateListener;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.form.handler.form.builder.IFaceBuilder;
import ru.sbsoft.client.components.form.model.ITehnologyModelAccessor;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <R>
 */
public abstract class SimpleFormFactory<M extends IFormModel, R extends MarkModel> extends BaseFormFactory<M, R> {

    public SimpleFormFactory(NamedFormType formType) {
        super(formType);
    }

    protected SimpleFormFactory(NamedFormContext ctx) {
        super(ctx);
    }

    @Override
    protected final SimpleFactoryForm createFormInstance() {
        return new SimpleFactoryForm();
    }

    protected void createHandlers(final SimpleFactoryForm form) {
    }

    protected void configForm(final SimpleFactoryForm form) {
    }

    protected final <A extends ITehnologyModelAccessor> FSet<M> createTehnologySet(A a) {
        return BaseValidatedForm.createTehnologySet(a);
    }

    protected abstract void buildFace(final IFaceBuilder<M> b, final SimpleFactoryForm form);

    protected void postBuildFace(final IFaceBuilder<M> b, final SimpleFactoryForm form) {
    }

    private void constructFace(final IFaceBuilder<M> b, final SimpleFactoryForm form) {
        createHandlers(form);
        buildFace(b, form);
        postBuildFace(b, form);
    }

    protected class SimpleFactoryForm extends BaseFactoryForm {

        private List<UpdateEditorsStateListener<M>> updateEditorsStateListeners;

        public SimpleFactoryForm() {
            updateEditorsStateListeners = new ArrayList<>();
        }

        @Override
        protected void buildFace(IFaceBuilder<M> b) {
            SimpleFormFactory.this.constructFace(b, this);
        }

        public final void addValidator(IFormValidator formValidator) {
            super.addFormValidator(formValidator);
        }

        public final void addUpdateEditorsStateListener(UpdateEditorsStateListener<M> l) {
            if (!updateEditorsStateListeners.contains(l)) {
                updateEditorsStateListeners.add(l);
            }
        }

        @Override
        protected void updateEditorsState() {
            super.updateEditorsState();
            for (UpdateEditorsStateListener<M> l : updateEditorsStateListeners) {
                l.updateEditorsState(this);
            }
        }

        @Override
        protected void onAfterFirstAttach() {
            super.onAfterFirstAttach();
            configForm(this);
        }

    }
    
}
