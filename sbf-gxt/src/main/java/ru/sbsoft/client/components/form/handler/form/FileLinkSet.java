package ru.sbsoft.client.components.form.handler.form;

import com.google.gwt.dom.client.Style;
import java.util.List;
import ru.sbsoft.client.components.field.FileLnkField;
import ru.sbsoft.client.components.form.BaseValidatedForm;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.sbf.gxt.components.VerticalFieldSet;
import ru.sbsoft.sbf.gxt.utils.FieldUtils;
import ru.sbsoft.shared.model.IFileLinksModel;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author sokolov
 * @author vk
 * @param <M> - model
 */
public class FileLinkSet<M extends IFormModel & IFileLinksModel> extends FSet<M> {

    public FileLinkSet(String cap, BaseValidatedForm<M> form) {
        super(cap);
        setCollapsible(true, true);
        form.addFromModelHandler(m -> {
            setVals(m != null ? m.getFileLnks() : null);
        });
    }

    public void setVals(List<LookupInfoModel> l) {
        VerticalFieldSet c = getManagedSet();
        c.clear();
        if (l != null && !l.isEmpty()) {
            l.forEach((t) -> {
                FileLnkField f = new FileLnkField();
                f.setHeight("" + FieldUtils.FIELD_HEIGHT + Style.Unit.PX.getType());
                f.setValue(t.getSemanticName());
                c.add(f);
            });
        }
    }
}
