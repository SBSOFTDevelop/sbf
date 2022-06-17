package ru.sbsoft.shared.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.grid.style.TextAlign;

/**
 * Содержит значения для преобразования програмного кода {@code key} в
 * отображаемое значение {@code value} для колонки таблицы. Регистрируется в
 * {@link RendererManager} при инициализании клиентской части приложения.
 *
 * @author balandin
 * @since May 12, 2014 4:39:37 PM
 */
public abstract class Renderer<T> {

    private static final String NOT_EXISTS_ELEMENT_MESSAGE = "ERROR";

    

    private TextAlign textAlign = TextAlign.center;

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public Renderer<T> setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public class Entry {

        private final T key;
        private final ILocalizedString value;
        private final ILocalizedString extendValue;

        public Entry(T key, ILocalizedString value, ILocalizedString extendValue) {
            this.key = key;
            this.value = value;
            this.extendValue = extendValue;
        }

        public T getKey() {
            return key;
        }

        public ILocalizedString getValue() {
            return value;
        }

        public ILocalizedString getExtendValue() {
            if (null == extendValue) {
                // для фильтров комбобокса
                return new NonLocalizedString("-");
            }
            return extendValue;
        }
    }

    protected List<Entry> items = new ArrayList<Entry>();
    protected Map<T, Entry> lookup;

    public Renderer() {
    }

    public List<Entry> getItems() {
        return items;
    }

    public void addItem(T key, ILocalizedString value) {
        addItem(key, value, value);
    }

    public void addItem(T key, String value) {
        addItem(key, value, value);
    }

    public void addItem(T key, ILocalizedString value, ILocalizedString extendedValue) {
        add(new Entry(key, value, extendedValue));
    }

    public void addItem(T key, String value, String extendedValue) {
        addItem(key, new NonLocalizedString(value), new NonLocalizedString(extendedValue));
    }

    public void add(Entry entry) {
        items.add(entry);
    }

    public ILocalizedString render(T value) {
        return render(value, new NonLocalizedString(NOT_EXISTS_ELEMENT_MESSAGE));
    }

    public ILocalizedString render(T value, ILocalizedString defValue) {
        if (lookup == null) {
            lookup = new HashMap<T, Entry>();
            for (Entry item : items) {
                lookup.put(item.getKey(), item);
            }
        }

        Entry entry = lookup.get(value);
        if (entry == null) {
            return defValue;
        }
        return entry.getValue();
    }

    public String getDefaultValue(T value) {
        return "[" + value + "]";
    }
}
