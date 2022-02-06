package ru.formsb.services;

import java.io.IOException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.web.multipart.MultipartFile;

public interface ConverterService {
    byte[] convertDocumentToHtml(MultipartFile inputFile, boolean nestLists) throws IOException, Docx4JException;
    byte[] convertHtmlToDocument(String html);
}
