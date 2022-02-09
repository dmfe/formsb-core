package ru.formsb.dtos.request;

import java.util.Arrays;

public enum Bank {
    SMP;

    public static Bank fromStr(String type) {
        return Arrays.stream(values())
                .filter(value -> value.toString().equalsIgnoreCase(type))
                .findFirst().orElse(null);
    }
}
