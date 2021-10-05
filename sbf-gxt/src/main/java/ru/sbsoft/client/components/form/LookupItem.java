package ru.sbsoft.client.components.form;

/**
 * Сохраняет пару ключ/значение для отображения в колонке таблицы.<br/>
 * {@code key} - значение, содержащееся в модели
 * {@link ru.sbsoft.shared.meta.Row} для соответствующий колонки.<br/>
 * {@code value} - значение, отображаемое пользователю. <br/>
 * Обычно формируется из значений заданного для колонки
 * {@link ru.sbsoft.shared.renderer.Renderer}.
 *
 * @author balandin
 * @since Jul 3, 2013 7:18:21 PM
 */
public class LookupItem {

    private final Object key;
    private final String value;
    private final boolean userInput;

    public LookupItem(Object key, String value) {
        this(key, value, false);
    }

    public LookupItem(Object key, String value, boolean userInput) {
        this.key = key;
        this.value = value;
        this.userInput = userInput;
    }

    public Object getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public boolean isUserInput() {
        return userInput;
    }
}
