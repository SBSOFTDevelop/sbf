package ru.sbsoft.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.sbsoft.common.Defs;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Jul 31, 2015
 */
public class Modifiers implements DTO {

    private Set<Modifier> items;

    public Modifiers() {
        this(null);
    }

    public Modifiers(Collection<Modifier> items) {
        this.items = new HashSet<Modifier>(Defs.coalesce(items, Collections.EMPTY_LIST));
    }
    
    public boolean isEmpty(){
        return items.isEmpty();
    }

    @Override
    public String toString() {
        return asString();
    }

    public String asString() {
        List<String> tmp = new ArrayList<String>();
        for (Modifier v : items) {
            if (v != null) {
                tmp.add(modifierToString(v));
            }
        }
        Collections.sort(tmp);
        return Strings.join(tmp.toArray(), "/");
    }

    private static String modifierToString(Modifier value) {
        if (value instanceof Enum) {
            return ((Enum) value).name();
        }
        return String.valueOf(value);
    }

    public void addItem(Modifier m) {
        items.add(m);
    }
    
    public Set<Modifier> getItems() {
        return items;
    }

    public void setItems(HashSet<Modifier> items) {
        this.items = items;
    }
}
