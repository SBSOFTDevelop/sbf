package ru.sbsoft.client.components.browser.filter.fields;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.ProtoType;
import static ru.sbsoft.shared.meta.ProtoType.DATETIME;

/**
 * Список всех доступных фильтров для гриды
 *
 * @author balandin
 * @since Jun 23, 2015
 */
public class HoldersList extends ArrayList<CustomHolder> {

    private Set<ProtoType> multyProtoTypes;
    private int index = 0;

    protected HoldersList() {
    }
    
    @Override
    public boolean add(CustomHolder e) {
        index++;
        e.setKey(String.valueOf(index));
        return super.add(e);
    }

    public Set<ProtoType> getMultyProtoTypes() {
        if (multyProtoTypes == null) {
            Counter counter = new Counter();
            for (CustomHolder h : this) {
                if (h instanceof FieldHolder) {
                    FieldHolder holder = (FieldHolder) h;
                    final ProtoType type = holder.getColumn().getColumn().getType().getProtoType();
                    switch (type) {
                        case TEXT:
                        case DATETIME:
                        case NUMERIC:
                            counter.count(type);
                            break;
                    }
                }
            }

            multyProtoTypes = EnumSet.noneOf(ProtoType.class);
            Set<ProtoType> types = counter.keySet();
            for (ProtoType t : types) {
                if (counter.get(t).getValue() > 1) {
                    multyProtoTypes.add(t);
                }
            }
        }
        return multyProtoTypes;
    }

    public CustomHolder findHolder(String alias) {
        for (int i = 0; i < size(); i++) {
            CustomHolder holder = get(i);
            if (holder instanceof FieldHolder) {
                FieldHolder h = (FieldHolder) holder;
                if (Strings.equals(alias, h.getColumn().getColumn().getAlias())) {
                    return h;
                }
            } else if (holder instanceof FilterDefinitionHolder) {
                FilterDefinitionHolder h = (FilterDefinitionHolder) holder;
                if (Strings.equals(alias, h.getDefinition().getAlias())) {
                    return h;
                }
            }
        }
        return null;
    }
}

class Counter extends HashMap<ProtoType, Counter.Item> {

    public void count(ProtoType type) {
        get(type).value++;
    }

    private Counter.Item get(ProtoType type) {
        Counter.Item item = super.get(type);
        if (item == null) {
            item = new Counter.Item();
            put(type, item);
        }
        return item;
    }

    static class Item {

        private int value = 0;

        public Item() {
        }

        public int getValue() {
            return value;
        }
    }
}
