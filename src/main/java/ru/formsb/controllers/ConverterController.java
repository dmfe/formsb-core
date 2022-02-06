package ru.formsb.controllers;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.formsb.services.ConverterService;

@RestController
@RequiredArgsConstructor
public class ConverterController {

    private final ConverterService converterService;

    @GetMapping("/test")
    public String test() {
        return "{\"message\": \"Hello, world!\"}";
    }

    @PostMapping(value = "/convert", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody byte[] convert(MultipartFile file, boolean nestLists) throws IOException, Docx4JException {
        return converterService.convertDocumentToHtml(file, nestLists);
    }
}
