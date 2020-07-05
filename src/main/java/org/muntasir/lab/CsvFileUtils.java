package org.muntasir.lab;

import org.muntasir.lab.exceptions.FatalParsingException;
import org.muntasir.lab.exceptions.LineParsingException;
import org.muntasir.lab.field.CsvField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.muntasir.lab.exceptions.FatalParsingException.*;
import static org.muntasir.lab.exceptions.FatalParsingException.OUTPUT_DIR_NOT_WRITABLE;

public class CsvFileUtils {

    private static final Logger LOG = Logger.getLogger(CsvFileUtils.class.getCanonicalName());

    private File outputDirectory;
    private File inputFile;

    public void verifyInput(File inputFile) throws FatalParsingException, FileNotFoundException {

        if (!inputFile.exists()) throw new FileNotFoundException(inputFile.getAbsolutePath() + " is not found");
        if (!inputFile.canRead()) throw new FatalParsingException(SOURCE_FILE_NOT_READABLE);
        this.inputFile = inputFile;
    }

    public void setOutputLocation(File outputDirectory) throws FatalParsingException {
        if (outputDirectory.exists()) {
            if (!outputDirectory.isDirectory()) throw new FatalParsingException(OUTPUT_NOT_DIRECTORY);
            if (!outputDirectory.canWrite()) throw new FatalParsingException(OUTPUT_DIR_NOT_WRITABLE);
            LOG.info("Destination directory is found and writable");
        } else {
            boolean result = outputDirectory.mkdirs();
            if (!result) {
                String message = "Failed to create output directory: " + outputDirectory.getAbsolutePath();
                LOG.severe(message);
                throw new FatalParsingException(OUTPUT_DIR_NOT_WRITABLE, message);
            }
        }
        this.outputDirectory = outputDirectory;
    }

    public String createOutputPattern() {
        // remove suffix
        String name = inputFile.getName();
        if (name.endsWith(".csv") || name.endsWith(".txt") || name.endsWith(".CSV") || name.endsWith(".TXT")) {
            name =  name.substring(0, name.length() - 4);
        }
        // add output dir path
        StringBuilder sb = new StringBuilder(outputDirectory.getAbsolutePath());
        sb.append(File.separator).append(name).append("-%08d.json");
        return sb.toString();
    }

    public File createJson(Set<CsvField> fieldSet, int fileCounter) throws LineParsingException {

        String outputFilePattern = createOutputPattern();
        String outputFilePath = String.format(outputFilePattern, fileCounter);
        LOG.fine("Output file path: " + outputFilePath);
        File outputFile = new File(outputFilePath);

        try {
            FileWriter writer = new FileWriter(outputFile, false);
            StringBuilder sb = new StringBuilder("{");

            Iterator<CsvField> iterator = fieldSet.iterator();
            while (iterator.hasNext()) {
                CsvField CsvField = iterator.next();
                if (CsvField.getStringValue() != null) { //skip for empty value
                    sb.append("\n\t")
                            .append("\"").append(CsvField.getName()).append("\" : ")
                            .append("\"").append(CsvField.getStringValue()).append("\"");

                    if (iterator.hasNext()) {
                        sb.append(",");
                    }
                }
            }
            sb.append("\n}");
            LOG.fine(sb.toString());
            writer.write(sb.toString());
            writer.flush();

        } catch (IOException ioException) {
            throw new LineParsingException("Unable to write " + outputFile.getAbsolutePath());
        }
        return outputFile;
    }
}
