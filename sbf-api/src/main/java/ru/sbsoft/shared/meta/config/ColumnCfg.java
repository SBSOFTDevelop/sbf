package ru.sbsoft.shared.meta.config;

import ru.sbsoft.shared.param.DTO;

/**
 * Класс представляет описание столбца грида.
 * <ul>
 * <li>alias - алиас</li>
 * <li>title- заголовок </li>
 * <li>description-описание </li>
 * <li>width-ширина </li>
 * <li>visible -видимость</li>
 * </ul>
 * 
 * @author balandin
 * @since May 18, 2015 3:04:28 PM
 */
public class ColumnCfg implements DTO {

    private String alias;
    private int width;
    private boolean visible;

    public ColumnCfg() {
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
