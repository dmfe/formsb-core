package ru.formsb.convertion;

import java.io.IOException;
import javax.xml.bind.JAXBException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.Bank;
import ru.formsb.dtos.request.DocType;

public interface Converter {
    byte[] convertDocumentToHtml(byte[] inputData, boolean nestLists) throws IOException, Docx4JException;
    ConversionData convertHtmlToDocument(String html, Bank bank) throws Docx4JException, JAXBException;
    DocType getExportType();
}
