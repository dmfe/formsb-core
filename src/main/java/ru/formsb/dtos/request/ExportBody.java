package ru.formsb.dtos.request;

import lombok.Data;

@Data
public class ExportBody {
    private String html;
    private DocType fileType;
    private Bank bank;
}
