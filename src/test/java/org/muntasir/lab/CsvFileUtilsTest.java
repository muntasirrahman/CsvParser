package org.muntasir.lab;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.muntasir.lab.field.CsvField;
import org.muntasir.lab.field.EmailField;
import org.muntasir.lab.field.NumberField;
import org.muntasir.lab.field.StringField;

import java.io.File;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class CsvFileUtilsTest {

    final String outputDirStr = "src/test/resources/outputDir";

    CsvFileUtils utils;

    @BeforeEach
    public void setUp() {
        utils = new CsvFileUtils();
    }

    @Test
    public void whenCsvFieldsParsedCreateJson() {

        String instituteCsv = "src/test/resources/institute.csv";

        assertDoesNotThrow(() -> {
            utils.verifyInput(new File(instituteCsv));
            utils.setOutputLocation(new File(outputDirStr));
        });

        TreeSet<CsvField> headers = new TreeSet<>();

        StringField fullName = new StringField("full_name");
        fullName.parseValue("Institute of Advanced Studies");
        headers.add(fullName);

        EmailField email = new EmailField("email");
        email.parseValue("info@ias.edu");
        headers.add(email);

        NumberField endowment = new NumberField("endowment");
        endowment.parseValue("741000111");
        headers.add(endowment);

        final File[] jsonFile = new File[1];
        assertDoesNotThrow(() -> {
            jsonFile[0] = utils.createJson(headers, 1);
        });

        assertNotNull(jsonFile[0]);
    }

    @Test
    public void whenCsvFileParsedThenConvert() {

        String peopleCsv = "src/test/resources/people.csv";
        File csvFile = new File(peopleCsv);
        File outputDir = new File(outputDirStr);

        assertDoesNotThrow(() -> {
            utils.verifyInput(csvFile);
            utils.setOutputLocation(outputDir);
        });

        String outputPathPattern = utils.createOutputPattern();
        System.out.println(outputPathPattern);

        CsvParser parser = new CsvParser();
        assertDoesNotThrow(() -> parser.convert(csvFile, outputDir));

    }
}
