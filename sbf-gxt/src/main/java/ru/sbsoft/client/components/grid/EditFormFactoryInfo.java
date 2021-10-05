package ru.sbsoft.client.components.grid;

import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class EditFormFactoryInfo {

    private boolean insertable = true;
    private String name = "";
    private FormUsageCondition formUsageCondition = COND_ANY;
    private FormFactory formFactory;

    public static final FormUsageCondition COND_ANY = new FormUsageCondition() {
        @Override
        public boolean isFormUsedForRow(Row row) {
            return true;
        }
    };

    public EditFormFactoryInfo() {
    }

    public EditFormFactoryInfo(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public EditFormFactoryInfo(boolean insertable, FormFactory formFactory) {
        this.insertable = insertable;
        this.formFactory = formFactory;
    }

    public EditFormFactoryInfo(boolean insertable, String name, FormFactory formFactory, FormUsageCondition formUsageCondition) {
        this.insertable = insertable;
        this.name = name;
        this.formFactory = formFactory;
        this.formUsageCondition = formUsageCondition;
    }

    public FormUsageCondition getFormUsageCondition() {
        return formUsageCondition;
    }

    public void setFormUsageCondition(FormUsageCondition formUsageCondition) {
        this.formUsageCondition = formUsageCondition;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormFactory getFormFactory() {
        return formFactory;
    }

    public void setFormFactory(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public static interface FormUsageCondition {

        boolean isFormUsedForRow(Row row);
    }

    public interface FormFactory {

        public BaseForm createForm();
    }

}
