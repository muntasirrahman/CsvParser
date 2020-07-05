package org.muntasir.lab.field;

import java.util.Objects;
import java.util.regex.Pattern;

public class PhoneField extends CsvField {

    private static final String REGEX = "(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";
    private static final Pattern pattern = Pattern.compile(REGEX);

    private String number;
    private boolean valid;

    public PhoneField(String name) {
        super();
        setName(name);
    }

    @Override
    public String getStringValue() {
        return number;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public void parseValue(String number) {
        this.valid = pattern.matcher(number).matches();
        this.number = number;

    }

    @Override
    public Object clone() {
        PhoneField field = new PhoneField(getName());
        field.setOrder(this.getOrder());
        if (number != null) field.parseValue(number);
        return field;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneField that = (PhoneField) o;
        return super.equals(o) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), number);
    }
}
