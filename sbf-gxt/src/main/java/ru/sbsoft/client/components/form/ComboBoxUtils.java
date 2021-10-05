package ru.sbsoft.client.components.form;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import java.util.EnumSet;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 * Фабрика объектов {@link ComboBox}
 *
 * @author balandin
 * @since Feb 13, 2015 4:43:39 PM
 */
public class ComboBoxUtils {

    final public static ModelKeyProvider<NamedItem> keyProvider = new ModelKeyProvider<NamedItem>() {
        @Override
        public String getKey(NamedItem item) {
            return item.getCode();
        }
    };

    final public static LabelProvider<NamedItem> labelProvider = new LabelProvider<NamedItem>() {
        @Override
        public String getLabel(NamedItem item) {
            return I18n.get(item.getItemName());
        }
    };

    final public static ModelKeyProvider<Enum> smartKeyProvider = new ModelKeyProvider<Enum>() {
        @Override
        public String getKey(Enum item) {
            return item.name();
        }
    };

    final public static LabelProvider<Enum> smartLabelProvider = new LabelProvider<Enum>() {
        @Override
        public String getLabel(Enum item) {
            try {
                return (String) FieldAccessUtils.getValue(item, "value");
            } catch (Exception ex) {
                return item.name();
            }
        }
    };

    public static <E extends Enum<E> & NamedItem> ComboBox<E> createEnumComboBox(Class<E> enumerationClass) {
        ComboBox<E> cb = createComboBox(smartKeyProvider, smartLabelProvider);
        fill(cb, enumerationClass);
        return cb;
    }

    public static <E extends Enum<E> & NamedItem> ComboBox<E> createNamedComboBox(Class<E> enumerationClass) {
        ComboBox<E> cb = createNamedComboBox();
        fill(cb, enumerationClass);
        return cb;
    }
    
    private static <E extends Enum<E>> void fill(ComboBox<E> cb, Class<E> enumerationClass){
        cb.getStore().addAll(EnumSet.allOf(enumerationClass));
    }
    
    public static <T extends NamedItem> ComboBox<T> createNamedComboBox() {
        return createComboBox(keyProvider, labelProvider);
    }
    
    private static <T> ComboBox<T> createComboBox(ModelKeyProvider<? super T> keyProvider, LabelProvider<? super T> labelProvider){
        ListStore<T> listStore = new ListStore<>(keyProvider);
        final ComboBox<T> comboBox = new ComboBox<T>(listStore, labelProvider);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        comboBox.setForceSelection(true);
        return comboBox;
    }
}
