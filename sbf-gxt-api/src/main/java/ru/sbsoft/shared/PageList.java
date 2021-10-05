package ru.sbsoft.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import ru.sbsoft.shared.meta.Wrapper;

/**
 * Класс представляет страницу результата грида, возвращаемую на клиент.
 * <p>Реализует механизм пагинации.
 * @see ru.sbsoft.dao.AbstractBuilder#getDataForBrowser
 * @author balandin
 * @param <T>
 */
public class PageList<T> extends ArrayList<T> implements Serializable {

    private int totalSize;
    private Map<String, Wrapper> aggs;

    public PageList() {
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public Map<String, Wrapper> getAggs() {
        return aggs;
    }

    public void setAggs(Map<String, Wrapper> aggs) {
        this.aggs = aggs;
    }
}
