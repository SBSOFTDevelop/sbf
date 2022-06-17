package ru.sbsoft.client.components.lookup;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import ru.sbsoft.svc.widget.core.client.form.TriggerField;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import java.util.List;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.model.LookupCellType;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * @author balandin
 * @since Jul 15, 2013 6:55:07 PM
 */
public class LookupComboBox extends TriggerField<String> {

    private final LookupField lookupField;
    private final LookupCellType lookupCellType;
    private final Validator<String> validator = new Validator<String>() {
        @Override
        public List<EditorError> validate(Editor<String> editor, String value) {

            final Style style = ((LookupComboBox) editor).getInputEl().getStyle();
            final LookupInfoModel model = (LookupInfoModel) lookupField.getValue();

            final String lookupValue = Strings.clean((model == null) ? null : getSemanticValueForModel(model, lookupCellType), true);

            value = Strings.clean(value, true);
            if (Strings.equals(lookupValue, value)) {
                style.clearColor();
                style.clearFontStyle();
            } else {
                style.setColor("#a00");
                style.setFontStyle(Style.FontStyle.ITALIC);
            }
            return null;
        }

        private String getSemanticValueForModel(LookupInfoModel model, LookupCellType lookupCellType) {
            final String semanticValue;
            switch (lookupCellType) {
                case KEY:
                    semanticValue = model.getSemanticKey();
                    break;
                case NAME:
                    semanticValue = model.getSemanticName();
                    break;
                default:
                    throw new IllegalStateException("Unknown cell type " + lookupCellType);
            }
            return semanticValue;
        }
    };

    public LookupComboBox(LookupBrowserCell cell) {
        super(cell);

        this.lookupField = cell.getLookupField();
        this.lookupCellType = cell.getLookupCellType();

        setAutoValidate(true);
        setValidationDelay(50);
        addValidator(validator);
    }

    public void collapse() {
        ((LookupBrowserCell) getCell()).collapse();
    }

    public void initValue(String value) {
        setValue(value);
        ((LookupBrowserCell) getCell()).initSearchHistory();
    }
}
