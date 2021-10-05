package ru.sbsoft.client.components.form.handler.form.builder;

import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 * @param <H>
 */
public class DualField<DataModel extends IFormModel, H extends IFieldHandler & IFormFieldHandler<? super DataModel>> {
    private final String label;
    private final H f1;
    private final H f2;
    private final Double part;

    public DualField(String label, H f1, H f2) {
        this(label, f1, f2, null);
    }
    
    public DualField(String label, H f1, H f2, Double part) {
        this.label = label;
        this.f1 = f1;
        this.f2 = f2;
        this.part = part;
    }

    public String getLabel() {
        return label;
    }

    public H getF1() {
        return f1;
    }

    public H getF2() {
        return f2;
    }

    public Double getPart() {
        return part;
    }

}
