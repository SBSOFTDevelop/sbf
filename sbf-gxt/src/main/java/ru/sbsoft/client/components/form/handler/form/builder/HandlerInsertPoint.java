package ru.sbsoft.client.components.form.handler.form.builder;

import com.sencha.gxt.widget.core.client.Component;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public class HandlerInsertPoint<DataModel extends FormModel> {

    private final FSet<DataModel> fSet;
    private String afterName;

    public HandlerInsertPoint(FSet<DataModel> fSet, String afterName) {
        this.fSet = fSet;
        this.afterName = afterName;
    }

    public <H extends IFieldHandler & IFormFieldHandler<DataModel>> HandlerInsertPoint<DataModel> add(H... fields) {
        if (fields != null && fields.length > 0) {
            for (H f : fields) {
                if (f != null) {
                    fSet.addAfter(afterName, f);
                    if (f.getLabel() != null) {
                        afterName = f.getLabel();
                    }
                }
            }
        }
        return this;
    }

    public <F extends Component> HandlerInsertPoint<DataModel> addField(String label, F f ){
        fSet.addFieldAfter(afterName, label, f);
        afterName = label;
        return this;
    }
}
