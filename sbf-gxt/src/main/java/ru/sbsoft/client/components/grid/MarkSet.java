package ru.sbsoft.client.components.grid;

import com.sencha.gxt.core.client.ValueProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Модель маркировки для таблицы. 
 * Хранит список маркированных элементов и предоставляет методы для работы с ним.
 * @author balandin
 * @since Jan 18, 2013 2:43:22 PM
 */
public class MarkSet<DataInfoModel extends MarkModel> {

    private final HashSet<BigDecimal> items = new HashSet<BigDecimal>();
    private final SystemGrid grid;

    public MarkSet(SystemGrid grid) {
        this.grid = grid;
    }

    private BigDecimal getID(DataInfoModel model) {
        final ValueProvider<DataInfoModel, BigDecimal> keyProvider = grid.getKeyProvider();
        return keyProvider.getValue(model);
    }

    public boolean isMarked(DataInfoModel model) {
        return items.contains(getID(model));
    }

    public void reload(Collection<BigDecimal> values) {
        items.clear();
        if (null != values) {
            items.addAll(values);
        }
    }

    public void inverse(DataInfoModel model) {
        final BigDecimal key = getID(model);
        if (items.contains(key)) {
            items.remove(key);
        } else {
            items.add(key);
        }
    }

    public boolean isSingleMark() {
        return items.size() == 1;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public ArrayList<BigDecimal> getMarkedRecords() {
        return new ArrayList<BigDecimal>(items);
    }

    public boolean contains(BigDecimal value) {
        return items.contains(value);
    }

    public boolean remove(BigDecimal value) {
        return items.remove(value);
    }

    public void clear() {
        items.clear();
    }

    public int size() {
        return items.size();
    }
}
