package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.client.components.form.handler.form.builder.IFaceBuilder;
import ru.sbsoft.client.components.form.model.IActRangeAccessor;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.model.IActRangeModel;
import ru.sbsoft.shared.model.ICreateInfoModel;
import ru.sbsoft.shared.model.IUpdateInfoModel;
import ru.sbsoft.client.components.form.model.ITehnologyModelAccessor;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public abstract class ActRangeFormFactory<M extends IFormModel & IActRangeModel & ICreateInfoModel & IUpdateInfoModel> extends GridFormFactory<M> {
    
    private boolean tehnologyExpand = false;

    protected ActRangeFormFactory(NamedFormType formType) {
        super(formType);
    }

    @Override
    protected final void postBuildFace(IFaceBuilder<M> b, SimpleFactoryForm form) {
        ITehnologyModelAccessor<M> a = GWT.create(ITehnologyModelAccessor.class);
        IActRangeAccessor<M> a2 = GWT.create(IActRangeAccessor.class);
        ActRangeFSet<M> arSet = new ActRangeFSet<>(a2);
        b.getMainTab()
                .add(arSet)
                .add(SimpleFactoryForm.createTehnologySet(a).setCollapsible(true, tehnologyExpand));
        super.postBuildFace(b, form);
        postBuildFace(b, form, arSet);
    }

    protected void setTehnologyExpand(boolean tehnologyExpand) {
        this.tehnologyExpand = tehnologyExpand;
    }

    protected void postBuildFace(IFaceBuilder<M> b, SimpleFactoryForm form, ActRangeFSet<M> actRangeSet) {
    }        
}
