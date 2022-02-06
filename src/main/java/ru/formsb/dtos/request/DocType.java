package ru.formsb.dtos.request;

import java.util.Arrays;

public enum DocType {
    DOCX,
    PDF;

    public static DocType fromStr(String type) {
        return Arrays.stream(values())
                .filter(value -> value.toString().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }
}
