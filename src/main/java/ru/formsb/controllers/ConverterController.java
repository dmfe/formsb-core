package ru.formsb.controllers;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.DocType;
import ru.formsb.dtos.request.ExportBody;
import ru.formsb.dtos.request.SmpData;
import ru.formsb.services.ConverterService;
import ru.formsb.services.impl.DocxService;
import ru.formsb.utils.FileUtils;

@RestController
@RequiredArgsConstructor
public class ConverterController {

    private final ConverterService converterService;
    private final DocxService docxService;

    @PostMapping(value = "/convert", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public byte[] convert(MultipartFile file, boolean nestLists) throws IOException, Docx4JException {
        return converterService.convertDocumentToHtml(file.getBytes(), getDocTypeByFileExtension(file), nestLists);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody ExportBody body)
            throws IOException, JAXBException, Docx4JException {

        ConversionData conversion = converterService.convertHtmlToDocument
                (body.getHtml(), body.getFileType(), body.getBank());

        prepareResponseWithAttachment(response, conversion);

        response.flushBuffer();
    }

    @GetMapping("/get-docx")
    public void getDocx(HttpServletResponse response) throws Docx4JException, IOException {
        ConversionData conversion = docxService.getDocx();

        prepareResponseWithAttachment(response, conversion);

        response.flushBuffer();
    }

    @PostMapping("/questionnaire")
    public void generateQuestionnaire(HttpServletResponse response, @RequestBody SmpData data)
            throws Docx4JException, IOException, JAXBException {

        ConversionData conversion = docxService.generateQuestionnaire(data);

        prepareResponseWithAttachment(response, conversion);

        response.flushBuffer();
    }

    private DocType getDocTypeByFileExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            return null;
        }

        return DocType.fromStr(FileUtils.getFileExtension(file.getOriginalFilename()));
    }

    private void prepareResponseWithAttachment(HttpServletResponse response, ConversionData data) throws IOException {
        response.addHeader("Content-disposition", "attachment;filename=" + data.getName());
        response.setContentType(data.getContentType());
        response.setContentLength(data.getLength());

        data.getStream().writeTo(response.getOutputStream());
    }
}
