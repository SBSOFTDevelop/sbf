package ru.sbsoft.client.components.tree;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.client.components.form.SimpleTreeFormFactory;
import ru.sbsoft.client.components.form.handler.form.TextHandler;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.form.handler.form.builder.IFaceBuilder;
import ru.sbsoft.client.components.form.handler.form.builder.Tab;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.model.TreeNodeSimpleFormModel;

/**
 *
 * @author Kiselev
 */
public class TreeNodeSimpleForm<M extends TreeNodeSimpleFormModel> extends SimpleTreeFormFactory<M> {

    public TreeNodeSimpleForm(NamedFormType formType) {
        super(formType);
    }

    @Override
    protected void buildFace(IFaceBuilder<M> b, SimpleFactoryForm form) {
        TreeNodeSimpleAccessor a = GWT.create(TreeNodeSimpleAccessor.class);
        b.add(new Tab<M>(getCaption())
                .add(new FSet<M>("-")
                        //.add(new LookupHandler<M>("Путь", a.parentNode()))
                        .add(new TextHandler<M>("Наименование", a.nodeName()).setReq())
                )
        );
    }

}
