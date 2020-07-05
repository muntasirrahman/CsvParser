package org.muntasir.lab.field;

import java.util.Objects;

public class NumberField extends CsvField {

    private double value = Double.MIN_VALUE;
    private boolean valid;

    public NumberField(String name) {
        super();
        setName(name);
    }
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
        this.valid = true;
    }

    @Override
    public String getStringValue() {
        return Double.toString(value);
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void parseValue(String value) {
        try {
            this.value = Double.parseDouble(value);
            this.valid = true;
        } catch (NumberFormatException ex) {
            this.valid = false;
        }
    }

    @Override
    public Object clone() {
        NumberField field = new NumberField(getName());
        field.setOrder(this.getOrder());
        field.setValue(value);
        return field;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberField that = (NumberField) o;
        return super.equals(o) && Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), value);
    }
}
