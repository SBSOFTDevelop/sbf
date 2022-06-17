package ru.sbsoft.client.filter.editor;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.button.CellButtonBase;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.button.ToggleButton;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.svc.widget.core.client.event.BlurEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import java.util.Arrays;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.SelectBaseBrowser;
import ru.sbsoft.client.components.browser.filter.FilterItem;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.ContextGrid;
import static ru.sbsoft.client.components.grid.GridMode.*;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.Condition;
import static ru.sbsoft.shared.Condition.*;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.filter.Dictionary;
import ru.sbsoft.shared.model.LookupInfoModel;

public class StringFilterAdapter extends RangeFilterAdapter {

    public static final String MARKER_BUTTON_WIDTH = "24px";
    //
    private final ToggleButton caseSensitiveButton = new CaseSensitiveCaption();
    private TextField lastFocusedField;

    public StringFilterAdapter(List<Condition> filterConditions) {
        super(FilterTypeEnum.STRING, filterConditions, createFieldEditor(), createFieldEditor());

        final BlurEvent.BlurHandler blurHandler = new BlurEvent.BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                lastFocusedField = (TextField) event.getSource();
            }
        };

        field1.addBlurHandler(blurHandler);
        field2.addBlurHandler(blurHandler);
    }

    public TextField getLastFocusedField() {
        if (pair()) {
            return ClientUtils.coalesce(lastFocusedField, (TextField) field1);
        } else {
            return (TextField) field1;
        }
    }

    @Override
    protected List<Condition> getDefaultConditionsList() {
        return Arrays.asList(LIKE,
                CONTAINS, STARTS_WITH, ENDS_WITH,
                EQUAL_ALT,
                GREATER, GREATER_OR_EQUAL,
                LESS, LESS_OR_EQUAL,
                IN_RANGE, IN_BOUND,
                EMPTY
        );
    }

    private class CaseSensitiveCaption extends ToggleButton {

//        public static final String CASE_SENSITIVE_CAPTION = "<span style='font-size:11px;'>Аб</span>";
//        public static final String CASE_UN_SENSITIVE_CAPTION = "<span style='font-size:11px;text-decoration:line-through;'>Аб</span>";
        public CaseSensitiveCaption() {
            super();

            setToolTip(I18n.get(SBFEditorStr.hintCaseSensitive));
            addValueChangeHandler(new ValueChangeHandler<Boolean>() {

                @Override
                public void onValueChange(ValueChangeEvent<Boolean> event) {
                    redrawCaption(event.getValue());
                }
            });
            redrawCaption(false);
        }

        @Override
        protected void onRedraw() {
            super.onRedraw();
            fixHeight(getElement());
        }

        private void redrawCaption(boolean pressed) {
            String ab = I18n.get(SBFGeneralStr.labelCharacters);
            if (pressed) {
                setText(ab);
            } else {
                getCell().setHTML(SafeHtmlUtils.fromTrustedString(new StringBuilder().append("<span style='text-decoration:line-through;'>").append(ab).append("</span>").toString()));
                redraw();
            }
//            String html = pressed
//                    ? "<span style='font-size:11px;'>" + I18n.get(SBFGeneralStr.labelCharacters) + "</span>"
//                    : "<span style='font-size:11px;text-decoration:line-through;'>" + I18n.get(SBFGeneralStr.labelCharacters) + "</span>";
//            getCell().setHTML(SafeHtmlUtils.fromTrustedString(html));
            setToolTip(pressed
                    ? I18n.get(SBFGeneralStr.labelCaseSensitive)
                    : I18n.get(SBFGeneralStr.labelCaseInsensitive));
        }
    }

    private static TextField createFieldEditor() {
        TextField f = new TextField();
        f.setAllowBlank(true);
        return f;
    }

    @Override
    public void build(IColumn column, FilterItem filterItem) {
        super.build(column, filterItem);

        final Dictionary dict = column.getDictionary();
        if (dict != null) {

            final TextButton button = new TextButton("…") {

                @Override
                protected void onRedraw() {
                    super.onRedraw();
                    fixHeight(getElement());
                }
            };
            button.setToolTip(I18n.get(SBFEditorStr.hintSelectDictionary));
            button.addSelectHandler(new SelectEvent.SelectHandler() {

                @Override
                public void onSelect(SelectEvent event) {
                    BaseGrid grid = new ContextGrid(dict.getGridType());
                    final SelectBaseBrowser browser = new SelectBaseBrowser<LookupInfoModel>(grid, HIDE_DELETE, HIDE_INSERT, HIDE_UPDATE, SINGLE_SELECTION) {

                        @Override
                        public void perfomSelectAction() {
                            final Row row = (Row) this.grid.getGrid().getSelectionModel().getSelectedItem();
                            getLastFocusedField().setValue(row.getString(Strings.coalesce(dict.getColumn(), "CODE_VALUE")));
                            onExit();
                        }
                    };
                    browser.show();
                    grid.checkInitialized();
                }
            });

            filterItem.add(resize(button), FilterItem.MARGINS);
        }

        buildModifiers(column, filterItem);
    }

    @Override
    public void buildModifiers(IColumn column, FilterItem filterItem) {
        //filterItem.add(resize(caseSensitiveButton), FilterItem.MARGINS);
        filterItem.add(caseSensitiveButton, FilterItem.MARGINS);
    }

    private SimpleContainer resize(CellButtonBase button) {
        button.setWidth(MARKER_BUTTON_WIDTH);
        SimpleContainer cnt = new SimpleContainer();
        cnt.getElement().getStyle().setPaddingBottom(3, Style.Unit.PX);
        cnt.setWidget(button);
        return cnt;
    }

    @Override
    public FilterInfo createFilterInfo() {
        final FilterInfo f = super.createFilterInfo();
        f.setCaseSensitive(caseSensitiveButton.getValue());
        return f;
    }

    @Override
    public void restoreControls(FilterInfo filterInfo) throws FilterSetupException {
        super.restoreControls(filterInfo);
        caseSensitiveButton.setValue(filterInfo.isCaseSensitive(), true, true);
    }

    private void fixHeight(XElement element) {
        final String t = element.getTagName();
        if (t != null && "TD".equalsIgnoreCase(t) && MARKER_BUTTON_WIDTH.equals(element.getStyle().getWidth())) {
            element.getStyle().setProperty("lineHeight", "normal");
            return;
        }

        for (int i = 0, count = element.getChildCount(); i < count; i++) {
            fixHeight(element.getChild(i).<XElement>cast());
        }
    }

    @Override
    protected Object checkValue(Object value) throws FilterSetupException {
        if (value instanceof String) {
            return value;
        }
        throw new FilterSetupException(I18n.get(SBFExceptionStr.typeMismatch), String.class, value);
    }
}
