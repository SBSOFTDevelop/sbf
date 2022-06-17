package ru.sbsoft.shared.grid.style;

/**
 *
 * @author Kiselev
 */
public enum FontWeight {

    bold, 
    bolder, 
    lighter, 
    normal,
    W100, 
    W200, 
    W300, 
    W400, 
    W500, 
    W600, 
    W700, 
    W800, 
    W900;
    
    public String getCss(){
        return name().startsWith("W") ? name().substring(1) : name();
    }
}
