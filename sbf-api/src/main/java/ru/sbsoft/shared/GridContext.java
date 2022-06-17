package ru.sbsoft.shared;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.sbsoft.shared.consts.BrowserMode;
import ru.sbsoft.shared.interfaces.GridType;

/**
 * Класс представляет контекст грида (сетки).
 * <p>Уникальный идентификатор контекста используется для связи грида
 * с шаблонами браузеров, наследниками {@link ru.sbsoft.dao.AbstractTemplate}.
 * 
 * @author panarin
 */
public class GridContext implements Serializable {

    private GridType gridType;
    private String context;
    private Modifiers modifiers;
    private List<FilterInfo> parentFilters = Collections.emptyList();
    private Set<String> exclusiveMods;
    private Map<String, Modifier> exclMods;

    public GridContext() {
        this(null, null);
    }

    public GridContext(GridType gridType, String context) {
        this.gridType = gridType;
        this.context = context;
        exclusiveMods = new HashSet<String>();
        exclusiveMods.add(BrowserMode.class.getName());
        modifiers = new Modifiers();
        exclMods = new HashMap<String, Modifier>();
    }

    public void setGridType(GridType gridType) {
        this.gridType = gridType;
    }

    public GridType getGridType() {
        return gridType;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setModifier(Modifier modifier) {
        if (modifier != null) {
             if (exclusiveMods.contains(modifier.getClass().getName())) {
                exclMods.put(modifier.getClass().getName(), modifier);
            } else {
                modifiers.addItem(modifier);
            }
        }
    }

    public void setModifiers(Collection<Modifier> modifiers) {
        if (modifiers != null) {
            for (Modifier m : modifiers) {
                setModifier(m);
            }
        }
    }

    public Modifiers getModifiers() {
        if (!(modifiers.isEmpty() && exclMods.isEmpty())) {
            Set<Modifier> res = new HashSet<Modifier>();
            res.addAll(modifiers.getItems());
            res.addAll(exclMods.values());
            return new Modifiers(res);
        }
        return new Modifiers();
    }
    
    public void setModifiers(Modifiers modifiers) {
        setModifiers(modifiers.getItems());
    }

    public void setModifiers(Modifier[] modifiers) {
        setModifiers(Arrays.asList(modifiers));
    }

    public List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    public void setParentFilters(List<FilterInfo> parentFilters) {
        this.parentFilters = parentFilters != null ? parentFilters : Collections.<FilterInfo>emptyList();
    }
}
