package ru.formsb.convertion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.UUID;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporter;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.springframework.stereotype.Component;
import ru.formsb.Configs.BaseConfig;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.Bank;
import ru.formsb.dtos.request.DocType;
import ru.formsb.utils.FileUtils;

@Component
@RequiredArgsConstructor
public class DocxConverter implements Converter {

    private static final String IMAGE_DIR = "/tmp/";
    private static final String TMP_FILE_PREFIX = "/tmp/formsb_tmp";
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private final BaseConfig baseConfig;

    @Override
    public byte[] convertDocumentToHtml(byte[] inputData, boolean nestLists)
            throws IOException, Docx4JException {
        if (inputData.length == 0) {
            return new byte[0];
        }

        File tmpFile = Paths.get(TMP_FILE_PREFIX + "_" + UUID.randomUUID().toString()).toFile();
        OutputStream inputDataOs = new FileOutputStream(tmpFile);
        inputDataOs.write(inputData);

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(tmpFile);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Docx4J.toHTML(configureHtmlSettings(wordMLPackage, baseConfig.getBaseUri() + IMAGE_DIR, nestLists),
                os, Docx4J.FLAG_NONE);

        return os.toByteArray();
    }

    @Override
    public ConversionData convertHtmlToDocument(String html, Bank bank) throws Docx4JException, JAXBException {
        ByteArrayOutputStream resultOs = new ByteArrayOutputStream();
        String resultFileName = FileUtils.generateDateFileName(bank.toString(), DocType.DOCX.toString().toLowerCase());

        WordprocessingMLPackage wordMLPackage = createWordMLPackage();

        XHTMLImporter xhtmlImporter = createXHTMLImporter(wordMLPackage);

        wordMLPackage.getMainDocumentPart().getContent().addAll(xhtmlImporter.convert(html, baseConfig.getBaseUri()));
        wordMLPackage.save(resultOs);

        return ConversionData.builder()
                .stream(resultOs)
                .name(resultFileName)
                .contentType(CONTENT_TYPE)
                .length(resultOs.size()).build();
    }

    @Override
    public DocType getExportType() {
        return DocType.DOCX;
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

    private WordprocessingMLPackage createWordMLPackage() throws Docx4JException, JAXBException {
        WordprocessingMLPackage result = WordprocessingMLPackage.createPackage();

        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();

        result.getMainDocumentPart().addTargetPart(ndp);

        ndp.unmarshalDefaultNumbering();

        return result;
    }

    private XHTMLImporter createXHTMLImporter(WordprocessingMLPackage wordMLPackage) {
        XHTMLImporter result = new XHTMLImporterImpl(wordMLPackage);
        result.setHyperlinkStyle("Hyperlink");

        return result;
    }
}
