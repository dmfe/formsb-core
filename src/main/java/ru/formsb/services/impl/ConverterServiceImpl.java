package ru.formsb.services.impl;

import static ru.formsb.utils.FileUtils.generateRandomFileName;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.formsb.services.ConverterService;

@Service
public class ConverterServiceImpl implements ConverterService {

    private static final String TMP_DIR = "/tmp/";
    private static final String IMAGE_DIR_SUFFIX = "_files";
    private static final int BUFFER_SIZE = 1024 * 8;

    @Override
    public byte[] convertDocumentToHtml(MultipartFile multipartFile, boolean nestLists)
            throws IOException, Docx4JException {

        if (multipartFile.getOriginalFilename() == null) {
            return new byte[0];
        }

        File tmpFile = Paths.get(TMP_DIR, generateRandomFileName(multipartFile.getOriginalFilename())).toFile();

        multipartFile.transferTo(tmpFile);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(tmpFile);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        OutputStream os = new BufferedOutputStream(bos, BUFFER_SIZE);
        Docx4J.toHTML(configureHtmlSettings(wordMLPackage, tmpFile.getAbsolutePath() + IMAGE_DIR_SUFFIX, nestLists),
                os, Docx4J.FLAG_NONE);

        return bos.toByteArray();
    }

    @Override
    public byte[] convertHtmlToDocument(String html) {
        return new byte[0];
    }

    private HTMLSettings configureHtmlSettings(WordprocessingMLPackage wordMLPackage,
                                               String imageDirPath,
                                               boolean nestLists) {
        HTMLSettings htmlSettings = new HTMLSettings();
        htmlSettings.setImageDirPath(imageDirPath);
        htmlSettings.setImageTargetUri(imageDirPath);
        htmlSettings.setOpcPackage(wordMLPackage);

        if (nestLists) {
            SdtWriter.registerTagHandler("HTML_ELEMENT", new SdtToListSdtTagHandler());
        } else {
            htmlSettings.getFeatures().remove(ConversionFeatures.PP_HTML_COLLECT_LISTS);
        }

        return htmlSettings;
    }

}
