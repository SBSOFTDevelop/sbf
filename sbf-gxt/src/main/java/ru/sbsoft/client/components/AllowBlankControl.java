package ru.sbsoft.client.components;

/**
 * Добавляет свойство 'allowBlank' (т.е. разшить/запретить пустое значение) к визуальным компонентам не унаследованным от {@link com.sencha.gxt.widget.core.client.form.ValueBaseField}
 * @author balandin
 * @since Jun 26, 2013 7:47:14 PM
 */
public interface AllowBlankControl {

    public void setAllowBlank(boolean value);

    public boolean isAllowBlank();
}
