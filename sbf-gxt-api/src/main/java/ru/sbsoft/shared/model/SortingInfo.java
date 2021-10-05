package ru.sbsoft.shared.model;

import java.io.Serializable;

public class SortingInfo implements Serializable {

    private String alias;
    private SortDirection sortDirection = SortDirection.ASC;

    public SortingInfo() {
    }

    public SortingInfo(String alias, SortDirection sortDirection) {
        this.alias = alias;
        this.sortDirection = sortDirection;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(final SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String createOrder() {
        return alias + " " + sortDirection.name();
    }

    @Override
    public String toString() {
        return createOrder();
    }
}
