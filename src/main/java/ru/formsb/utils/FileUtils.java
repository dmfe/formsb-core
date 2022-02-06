package ru.formsb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class FileUtils {

    private static final String FILE_EXTENSION = "\\.";
    private static final String SEPARATOR = "_";
    private static final String EXTENSION_SEPARATOR = ".";
    private static final String DATE_PATTERN = "yyyy-MM-dd";

    private FileUtils() {
    }

    public static String getFileExtension(String fileName) {
        String[] parts = fileName.split(FILE_EXTENSION);

        return parts.length > 1 ? parts[1] : "";
    }

    public static String generateDateFileName(String prefix, String extension) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);

        return prefix + SEPARATOR + simpleDateFormat.format(new Date()) + EXTENSION_SEPARATOR + extension;
    }
}
