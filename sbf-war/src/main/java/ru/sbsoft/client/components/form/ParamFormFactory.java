package ru.sbsoft.client.components.form;

import com.google.gwt.resources.client.ImageResource;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.sbf.app.model.FormModel;

/**
 *
 * @author Kiselev
 */
public abstract class ParamFormFactory extends AbstractParamFormFactory {

    private ImageResource icon = null;

    protected ParamFormFactory(OperationType operationType) {
        super(operationType);
    }

    protected ParamFormFactory(String header) {
        super(header);
    }

    protected ParamFormFactory(I18nResourceInfo header) {
        this(I18n.get(header));
    }

    public void setIcon(ImageResource icon) {
        this.icon = icon;
    }

    @Override
    public GeneralParamForm createForm() {
        return createParamFormInstance();
    }

    protected ParamForm createParamFormInstance() {
        return new ParamForm();
    }

    protected abstract void fillHandlers(final ParamHandlerCollector h, final ParamForm f);

    protected class ParamForm extends GeneralParamForm implements IParentGridAware<Row>, IParentFormAware<FormModel> {

        private BaseGrid<Row> parentGrid = null;
        private BaseForm<FormModel> parentForm = null;

        public ParamForm() {
            super(ParamFormFactory.this.getHeader());
            if (icon != null) {
                getWindow().getHeader().setIcon(icon);
            }
        }

        protected void beforeAddHandlers(ParamHandlerCollector hc) {
        }

        @Override
        protected void addHandlers(ParamHandlerCollector hc) {
            beforeAddHandlers(hc);
            fillHandlers(hc, this);
            afterAddHandlers(hc);
        }

        protected void afterAddHandlers(ParamHandlerCollector hc) {
        }

        @Override
        public void setParentGrid(BaseGrid<Row> g) {
            this.parentGrid = g;
        }

        public BaseGrid<Row> getParentGrid() {
            return parentGrid;
        }

        @Override
        public void setParentForm(BaseForm<FormModel> f) {
            this.parentForm = f;
        }

        public BaseForm<FormModel> getParentForm() {
            return parentForm;
        }

    }

}
