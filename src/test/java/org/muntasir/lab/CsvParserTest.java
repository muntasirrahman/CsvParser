package org.muntasir.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.muntasir.lab.exceptions.FatalParsingException;
import org.muntasir.lab.field.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CsvParserTest {

    private CsvParser parser;

    @BeforeEach
    public void setUp() {
        parser = new CsvParser();
    }

    @Test
    public void whenDuplicateFieldNameFoundThrowException() {
        String duplicateFieldsLine = "product name, price, phone, email, price :number";
        assertThrows(FatalParsingException.class, () -> parser.parseHeader(duplicateFieldsLine));
    }

    @Test
    public void whenReadHeaderLineReturnSet() throws IOException, FatalParsingException {

        String line = "product name, email, phone, price :number";
        Set<CsvField> fields = parser.parseHeader(line);

        assertTrue(fields.size() > 0);

        Iterator<CsvField> iterator = fields.iterator();
        CsvField field1 = iterator.next();
        assertEquals("product name", field1.getName());
        assertTrue(field1 instanceof StringField);

        CsvField field2 = iterator.next();
        assertEquals("email", field2.getName());
        assertTrue(field2 instanceof EmailField);

        CsvField field3 = iterator.next();
        assertEquals("phone", field3.getName());
        assertTrue(field3 instanceof PhoneField);

        CsvField  field4 = iterator.next();
        assertEquals("price", field4.getName());
        assertTrue(field4 instanceof NumberField);

    }

    @Test
    public void whenReadDataLineReturnUpdatedField() throws FatalParsingException {
        String headers = "product, info :email, support :phone, price :number";
        Set<CsvField> fields = parser.parseHeader(headers);

        String line = "Fanta Orange, info@cocacola.com, 18001234567, 1.5";
        parser.parseLineAndUpdateFields(line, fields);

        Iterator<CsvField> iterator = fields.iterator();
        CsvField  field1 = iterator.next();
        assertEquals("Fanta Orange", field1.getStringValue());

        CsvField  field2 = iterator.next();
        assertTrue(field2.isValid());

        CsvField  field3 = iterator.next();
        assertTrue(field3.isValid());

        CsvField  field4 = iterator.next();
        assertTrue(field4.isValid());
    }

    @Test
    public void whenFeedMultipleLinesReturnFields() throws FatalParsingException {

        String[] line = new String[3];
        line[0] = "product, info :email, support :phone, price :number";

        Set<CsvField> fields1 = parser.parseHeader(line[0]);
        line[1] = "Fanta Orange, info1@cocacola.com, 18001234567, 1.5";
        parser.parseLineAndUpdateFields(line[1], fields1);

        line[2] = "Heaven & Earth Lemon Tea, info2@cocacola.com, 6598765432, 2.0";
        Set<CsvField> fields2 = parser.clone(fields1);
        parser.parseLineAndUpdateFields(line[2], fields2);

        Iterator<CsvField> iterator1 = fields1.iterator();
        Iterator<CsvField> iterator2 = fields2.iterator();

        for (int i = 0; i < fields1.size(); i++) {
            CsvField f1 = iterator1.next();
            CsvField f2 = iterator2.next();
            assertNotSame(f1, f2);
            assertNotEquals(f1.getStringValue(), f2.getStringValue());
        }
    }
}
