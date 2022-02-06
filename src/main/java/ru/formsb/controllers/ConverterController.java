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
import ru.formsb.services.ConverterService;
import ru.formsb.utils.FileUtils;

@RestController
@RequiredArgsConstructor
public class ConverterController {

    private final ConverterService converterService;

    @GetMapping("/test")
    public String test() {
        return "{\"message\": \"Hello, world!\"}";
    }

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

        response.addHeader("Content-disposition", "attachment;filename=" + conversion.getName());
        response.setContentType(conversion.getContentType());
        response.setContentLength(conversion.getLength());

        conversion.getStream().writeTo(response.getOutputStream());

        response.flushBuffer();
    }

    private DocType getDocTypeByFileExtension(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            return null;
        }

        return DocType.fromStr(FileUtils.getFileExtension(file.getOriginalFilename()));
    }
}
