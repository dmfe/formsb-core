package ru.formsb.utils;

import java.util.UUID;

public final class FileUtils {

    private static final String FILE_EXTENSION = "\\.";
    private static final String SEPARATOR = "_";

    private FileUtils() {}

    public static String generateRandomFileName(String fileName) {
        String[] parts = fileName.split(FILE_EXTENSION);

        return parts[0] + SEPARATOR + UUID.randomUUID().toString() + SEPARATOR + parts[1];
    }
}
