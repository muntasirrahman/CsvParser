package org.muntasir.lab;

import org.muntasir.lab.exceptions.FatalParsingException;
import org.muntasir.lab.exceptions.LineParsingException;
import org.muntasir.lab.field.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static org.muntasir.lab.exceptions.FatalParsingException.DUPLICATE_FIELD_NAME;
import static org.muntasir.lab.exceptions.FatalParsingException.HEADER_NOT_FOUND;

public class CsvParser {

    private static final Logger LOG = Logger.getLogger(CsvParser.class.getCanonicalName());
    private static final int EXIT_FROM_ERROR = 1;

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: \tjava -jar CsvParser.jar <input csv> <optional output directory>"
                    + "\nExample:"
                    + "\n\tjava -jar CsvParser.jar product.csv");
            System.exit(EXIT_FROM_ERROR);
        }

        LOG.setLevel(Level.INFO);
        try {
            File csvFile = new File(args[0]);
            File outputDirectory = args.length < 2
                    ? Paths.get("").toAbsolutePath().toFile()
                    : new File(args[1]);

            CsvParser parser = new CsvParser();
            parser.convert(csvFile, outputDirectory);

        } catch (FileNotFoundException e) {
            LOG.severe("File not found: " + e.getMessage());

        } catch (FatalParsingException e) {
            LOG.severe(e.getMessage());
        }
    }

    public void convert(File csvFile, File outputDir) throws FileNotFoundException, FatalParsingException {

        CsvFileUtils fileUtils = new CsvFileUtils();
        fileUtils.verifyInput(csvFile);
        fileUtils.setOutputLocation(outputDir);

        BufferedReader reader = new BufferedReader(new FileReader(csvFile));

        // csv file headers
        Set<CsvField> headerFields;
        try {
            String headerLine = reader.readLine();
            headerFields = parseHeader(headerLine);
        } catch (IOException e) {
            throw new FatalParsingException(HEADER_NOT_FOUND, "Header line is not found at " + csvFile.getAbsolutePath());
        }

        int lineCounter = 0;
        String line;
        while (true) {
            try {
                line = reader.readLine();
                if (line == null) break;
                if (line.isBlank()) continue;
                ++lineCounter;

                Set<CsvField> lineFields = clone(headerFields);
                parseLineAndUpdateFields(line, lineFields);
                fileUtils.createJson(lineFields, lineCounter);

            } catch (LineParsingException parsingException) {
                LOG.warning(parsingException.getMessage());
            } catch (IOException ex) {
                LOG.warning(String.format("Unable to read line %d from file %s message: %s",
                        lineCounter, csvFile.getAbsolutePath(), ex.getMessage()));
            }
        }
    }

    protected Set<CsvField> parseHeader(String line) throws FatalParsingException {

        String[] names = line.split(",");
        Set<String> fieldNames = new HashSet<>();

        TreeSet<CsvField> set = new TreeSet<>();
        for (int i = 0; i < names.length; i++) {
            String name = names[i].trim();
            CsvField field;

            if ("email".equalsIgnoreCase(name)) {
                checkDuplicateAndAdd(fieldNames, name);
                field = new EmailField(name);

            } else if (name.endsWith(":email")) {
                String adjustedName = name.substring(0, name.indexOf(":email")).trim();
                checkDuplicateAndAdd(fieldNames, name);
                field = new EmailField(adjustedName);

            } else if ("phone".equalsIgnoreCase(name)) {
                checkDuplicateAndAdd(fieldNames, name);
                field = new PhoneField(name);

            } else if (name.endsWith(":phone")) {
                String adjustedName = name.substring(0, name.indexOf(":phone")).trim();
                checkDuplicateAndAdd(fieldNames, adjustedName);
                field = new PhoneField(adjustedName);

            } else if (name.endsWith(":number")) {
                String adjustedName = name.substring(0, name.indexOf(":number")).trim();
                checkDuplicateAndAdd(fieldNames, adjustedName);
                field = new NumberField(adjustedName);

            } else {
                checkDuplicateAndAdd(fieldNames, name);
                field = new StringField(name);
            }

            field.setOrder(i);
            set.add(field);
        }
        return set;
    }

    /**
     *
     * @param set
     * @param name
     * @return true if there is no duplication
     * @throws FatalParsingException if there is a duplicate field name
     */
    private boolean checkDuplicateAndAdd(Set<String> set, String name) throws FatalParsingException {
        if (set.contains(name)) {
            throw new FatalParsingException(DUPLICATE_FIELD_NAME, "Duplicate field name: " + name);
        }
        set.add(name);
        return true;
    }

    protected Set<CsvField> parseLineAndUpdateFields(String line, Set<CsvField> csvFields) {

        Iterator<CsvField> iterator = csvFields.iterator();
        String[] word = line.split(",");

        for (int i = 0; i < word.length; i++) {
            CsvField field = iterator.next();
            field.parseValue(word[i].trim());
        }
        return csvFields;
    }

    /**
     * Deep cloning operation. The implementation might be different among CsvField extensions.
     * @param originalSet original set
     * @return cloning result
     */
    public Set<CsvField> clone(Set<CsvField> originalSet) {
        return originalSet.stream()
                .map(field -> (CsvField) field.clone())
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
