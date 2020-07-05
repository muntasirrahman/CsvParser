package org.muntasir.lab.field;

public abstract class CsvField implements Comparable<CsvField>, Cloneable {

    private int order;
    private String name;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getStringValue();

    public abstract Object clone();
    public abstract boolean isValid();
    public abstract void parseValue(String value);

    @Override
    public int compareTo(CsvField o) {
        if (order == o.getOrder()) {
            return name.compareTo(o.getName());
        }
        return order > o.getOrder() ? 1 : -1;
    }
}

