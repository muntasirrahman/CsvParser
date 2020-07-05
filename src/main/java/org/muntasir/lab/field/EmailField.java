package org.muntasir.lab.field;

import java.util.Objects;
import java.util.regex.Pattern;

public class EmailField extends CsvField {

    private static String REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    private static Pattern pattern = Pattern.compile(REGEX);

    private String address;

    private boolean valid;

    public EmailField(String name) {
        setName(name);
    }

    @Override
    public String getStringValue() {
        return address;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void parseValue(String address) {
        this.address = address;
        this.valid = pattern.matcher(address).matches();
    }

    @Override
    public Object clone() {
        EmailField field = new EmailField(getName());
        field.setOrder(this.getOrder());
        if (address != null) field.parseValue(address);
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EmailField that = (EmailField) o;
        return super.equals(o) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), address);
    }
}
