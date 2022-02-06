package ru.formsb.services.impl;

import java.io.IOException;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.stereotype.Service;
import ru.formsb.convertion.ConverterFactory;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.Bank;
import ru.formsb.dtos.request.DocType;
import ru.formsb.services.ConverterService;

@Service
@RequiredArgsConstructor
public class ConverterServiceImpl implements ConverterService {

    private final ConverterFactory converterFactory;

    @Override
    public byte[] convertDocumentToHtml(byte[] inputData, DocType type, boolean nestLists)
            throws IOException, Docx4JException {
        return converterFactory.getConverter(type).convertDocumentToHtml(inputData, nestLists);
    }

    @Override
    public ConversionData convertHtmlToDocument(String html, DocType type, Bank bank)
            throws Docx4JException, JAXBException {
        return converterFactory.getConverter(type).convertHtmlToDocument(html, bank);
    }
}
