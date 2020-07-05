package org.muntasir.lab.field;

import java.util.Objects;

public class StringField extends CsvField {

    private String value;

    public StringField(String name) {
        super();
        setName(name);
    }

    @Override
    public String getStringValue() {
        return value;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void parseValue(String value) {
        this.value = value;
    }

    @Override
    public Object clone() {
        StringField field = new StringField(getName());
        field.setOrder(this.getOrder());
        field.parseValue(value);
        return field;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StringField that = (StringField) o;
        return super.equals(o) && Objects.equals(value, that.value);
    }


    @Override
    public int hashCode() {
        return Objects.hash(getName(), value);
    }
}
