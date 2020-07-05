package org.muntasir.lab;

import org.junit.jupiter.api.Test;
import org.muntasir.lab.field.EmailField;
import org.muntasir.lab.field.NumberField;
import org.muntasir.lab.field.PhoneField;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvFieldTest {

    @Test
    public void whenEmailFieldParsed() {
        EmailField field1 = new EmailField("email");
        field1.parseValue("info@yahoo.com");
        assertTrue(field1.isValid());

        field1.parseValue("www.yahoo.com");
        assertFalse(field1.isValid());
    }

    @Test
    public void whenPhoneFieldParsed() {
        PhoneField field1 = new PhoneField("mobile");
        field1.parseValue("6590237840");
        assertTrue(field1.isValid());

        field1.parseValue("1800-HELLO");
        assertFalse(field1.isValid());
    }

    @Test
    public void whenNumberFieldParsed() {
        NumberField field1 = new NumberField("zip");
        field1.parseValue("0800123");
        assertTrue(field1.isValid());

        field1.parseValue("-800123");
        assertTrue(field1.isValid());

        field1.parseValue("7.23e2");
        assertTrue(field1.isValid());

        field1.parseValue("7.23e-2");
        assertTrue(field1.isValid());

        field1.parseValue("1800-9000");
        assertFalse(field1.isValid());
    }
}
