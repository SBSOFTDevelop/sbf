package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import java.util.Arrays;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.sbf.gxt.components.FieldsContainer;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.shared.ComparisonEnum;
import static ru.sbsoft.shared.Condition.*;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Поле для диалога фильтра таблицы. 
 * По сравнению с {@link TextField} дополнено опциями текстового поиска: чувствительность к регистру, поиск подстроки, поиск по шаблону и т.д.
 * @author balandin
 * @since Sep 18, 2013 11:44:50 AM
 */
public class TextFilterField extends FieldsContainer {

    private final String columnName;
    //
    protected final SimpleComboBox<Condition> conditions;
    protected final CheckBox checkBox;
    protected final TextField textField;
    //
    protected final static List<Condition> CONDITIONS_LIST = Arrays.asList(
        LIKE,
        CONTAINS, STARTS_WITH, ENDS_WITH,
        EQUAL_ALT
//        GREATER, GREATER_OR_EQUAL,
//        LESS, LESS_OR_EQUAL,
//        IS_NULL
    );

    public TextFilterField(String columnName) {
        super();

        this.columnName = columnName;

        conditions = new SimpleComboBox<Condition>(new LabelProvider<Condition>() {
            @Override
            public String getLabel(Condition item) {
                return I18n.get(item);
            }
        });
        conditions.setEditable(false);
        conditions.setAllowBlank(false);
        conditions.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        conditions.setWidth(135);
        conditions.add(CONDITIONS_LIST);

        checkBox = new CheckBox();
        checkBox.setToolTip(I18n.get(SBFEditorStr.hintCaseSensitive));

        textField = new TextField();

        HorizontalPanel hp = new HorizontalPanel();
        hp.setSpacing(4);
        hp.add(checkBox);

        add(conditions, HLC.CONST);
        add(hp, HLC.CONST);
        add(textField, HLC.FILL);
    }

    public SimpleComboBox<Condition> getConditions() {
        return conditions;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public TextField getTextField() {
        return textField;
    }

    public FilterInfo createFilter() {
        final FilterInfo filter = new StringFilterInfo();
        filter.setComparison(conditions.getValue().getComparison());
        filter.setColumnName(columnName);
        filter.setValue(textField.getCurrentValue());
        filter.setCaseSensitive(checkBox.getValue());
        return filter;
    }

    public void setFilterInfo(FilterInfo config) {
        conditions.setValue(getComparison(config.getComparison()));
        checkBox.setValue(config.isCaseSensitive());
        textField.setValue((String) config.getValue());
    }

    public Condition getComparison(ComparisonEnum comparisonEnum) {
        if (comparisonEnum == null) {
            comparisonEnum = ComparisonEnum.startswith;
        }
        switch (comparisonEnum) {
            case eq:
                return EQUAL_ALT;
            case like:
                return LIKE;
            case contains:
                return CONTAINS;
            case startswith:
                return STARTS_WITH;
            case endswith:
                return ENDS_WITH;
        }
        return STARTS_WITH;
    }

    public void reinit() {
        textField.setValue(null);
        conditions.setValue(Condition.STARTS_WITH);
        checkBox.setValue(true);
    }
}
