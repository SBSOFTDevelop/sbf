package ru.sbsoft.shared.meta;

import java.io.Serializable;
import java.util.List;

/**
 * Класс служит для задания стиля колонки грида.
 * <p>Например:
 * <pre>
 * ColumnsInfo c = new ColumnsInfo();
 * final Style rowStyle2 = new Style("background: #FAF3D2;", "CALC_TYPE.indexOf('Итог_квартал') == 0");
 * c.addStyle(rowStyle);
 * </pre>
 * @author balandin
 * @since Oct 22, 2014 6:49:08 PM
 * @deprecated используйте {@link ru.sbsoft.shared.grid.style.CStyle} с {@link ru.sbsoft.shared.grid.condition.IGridCondition}
 */
public class Style implements Serializable {

    private String template;
    private String condition;
    //
    private transient boolean valid = true;
    private transient List<String> params;

    public Style() {
        this(null, null);
    }

    public Style(String template, String condition) {
        this.template = template;
        this.condition = condition;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }
}
