package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.param.DTO;

public class UpdateInfo implements DTO {

    private String table;
    private String column;
    
    //для BigDecimal length == BigDecimal.getScale
    private int lenght;

    public UpdateInfo() {
    }

    public UpdateInfo(String table, String column) {
        this(table, column, 0);
    }

    public UpdateInfo(String table, String column, int lenght) {
        this.table = table;
        this.column = column;
        this.lenght = lenght;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }
}
