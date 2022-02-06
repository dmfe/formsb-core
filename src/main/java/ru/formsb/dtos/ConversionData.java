package ru.formsb.dtos;

import java.io.ByteArrayOutputStream;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConversionData {
    private ByteArrayOutputStream stream;
    private String name;
    private String contentType;
    private int length;
}
