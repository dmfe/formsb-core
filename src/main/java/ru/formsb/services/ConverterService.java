package ru.formsb.services;

import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.Bank;
import ru.formsb.dtos.request.DocType;

public interface ConverterService {
    byte[] convertDocumentToHtml(byte[] inputData, DocType type, boolean nestLists) throws IOException, Docx4JException;
    ConversionData convertHtmlToDocument(String html, DocType type, Bank bank)
            throws IOException, Docx4JException, JAXBException;
}
