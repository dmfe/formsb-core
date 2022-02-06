package ru.formsb.convertion;

import org.springframework.stereotype.Component;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.Bank;
import ru.formsb.dtos.request.DocType;

@Component
public class PdfConverter implements Converter {

    @Override
    public byte[] convertDocumentToHtml(byte[] inputData, boolean nestLists) {
        throw new UnsupportedOperationException("PDF conversion is not supported");
    }

    @Override
    public ConversionData convertHtmlToDocument(String html, Bank bank) {
        throw new UnsupportedOperationException("PDF conversion is not supported");
    }

    @Override
    public DocType getExportType() {
        return DocType.PDF;
    }
}
