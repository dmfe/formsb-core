package ru.formsb.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;
import org.springframework.stereotype.Service;
import ru.formsb.Configs.BaseConfig;
import ru.formsb.dtos.ConversionData;
import ru.formsb.dtos.request.Bank;
import ru.formsb.dtos.request.DocType;
import ru.formsb.dtos.request.SmpData;
import ru.formsb.utils.FileUtils;

@Service
@RequiredArgsConstructor
public class DocxService {

    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    private final BaseConfig baseConfig;

    public ConversionData getDocx() throws Docx4JException {

        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
        mainDocumentPart.addStyledParagraphOfText("Title", "Hello, World!");
        mainDocumentPart.addParagraphOfText("Welcome to DMFE");

        return generateConversionData(wordMLPackage);
    }

    public ConversionData generateQuestionnaire(SmpData data) throws Docx4JException, JAXBException {
        String templateFileName = baseConfig.getDocxTemplatesMap().get(Bank.SMP);
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new File(templateFileName));

        MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();

        String fullOrganisationNameXpath = "//w:t[starts-with(text(), 'FRSB')]";

        Map<String, JAXBElement> textNodes =
                mainDocumentPart.getJAXBNodesViaXPath(fullOrganisationNameXpath, true).stream()
                        .map(JAXBElement.class::cast)
                        .collect(Collectors.toMap(
                                element -> String.valueOf(((Text) element.getValue()).getValue()),
                                Function.identity()
                        ));

        setDocumentNodes(textNodes, data);

        return generateConversionData(wordMLPackage);
    }

    private ConversionData generateConversionData(WordprocessingMLPackage wordMLPackage) throws Docx4JException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        wordMLPackage.save(os);

        String resultFileName = FileUtils.generateDateFileName("test", DocType.DOCX.toString().toLowerCase());

        return ConversionData.builder()
                .stream(os)
                .name(resultFileName)
                .contentType(CONTENT_TYPE)
                .length(os.size())
                .build();
    }

    private void setDocumentNodes(Map<String, JAXBElement> textNodes, SmpData data) {
        textNodes.get("FRSB_FULL_ORG_NAME").setValue(createTextElement(data.getOrgFullName()));
        textNodes.get("FRSB_UL_IND").setValue(createTextElement(data.getLegalEntityIndex()));
        textNodes.get("FRSB_UL_CITY").setValue(createTextElement(data.getLegalEntityCity()));
        textNodes.get("FRSB_UL_STREET").setValue(createTextElement(data.getLegalEntityStreet()));
        textNodes.get("FRSB_UL_BUILDING").setValue(createTextElement(data.getLegalEntityBuilding()));
        textNodes.get("FRSB_UL_CORP").setValue(createTextElement(data.getLegalEntityCorp()));
        textNodes.get("FRSB_PST_IND").setValue(createTextElement(data.getPostIndex()));
        textNodes.get("FRSB_PST_CITY").setValue(createTextElement(data.getPostCity()));
        textNodes.get("FRSB_PST_STREET").setValue(createTextElement(data.getPostStreet()));
        textNodes.get("FRSB_PST_BUILDING").setValue(createTextElement(data.getPostBuilding()));
        textNodes.get("FRSB_PST_CORP").setValue(createTextElement(data.getPostCorp()));
    }

    private Text createTextElement(String value) {
        Text result = new Text();
        result.setValue(value);

        return result;
    }
}
