package org.muntasir.lab.exceptions;

public class FatalParsingException extends Exception {

    public static final int UNKNOWN = 0;
    public static final int SOURCE_FILE_NOT_READABLE = 1;
    public static final int OUTPUT_NOT_DIRECTORY = 2;
    public static final int OUTPUT_DIR_NOT_WRITABLE = 3;
    public static final int DUPLICATE_FIELD_NAME = 4;
    public static final int HEADER_NOT_FOUND = 5;

    private int errorCode = 0;

    public FatalParsingException(int errorCode) {
        this.errorCode = errorCode;
    }

    public FatalParsingException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
