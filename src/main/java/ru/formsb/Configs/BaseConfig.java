package ru.formsb.Configs;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.formsb.dtos.request.Bank;

@Configuration
@Getter
public class BaseConfig {

    private static final String TEMPLATES_SEPARATOR = ";";
    private static final String TEMPLATES_KV_SEPARATOR = ":";

    private Map<Bank, String> docxTemplatesMap;

    private Environment env;

    @Value("${base.uri}")
    private String baseUri;

    public BaseConfig(Environment environment) {
        env = environment;
    }

    @PostConstruct
    private void init() {
        docxTemplatesMap = readTemplates();
    }

    private Map<Bank, String> readTemplates() {
        String templates = env.getProperty("templates.docx");

        if (templates == null) {
            return Collections.emptyMap();
        }

        return Arrays.stream(templates.split(TEMPLATES_SEPARATOR))
                .filter(template -> template.split(TEMPLATES_KV_SEPARATOR).length == 2)
                .map(template -> {
                    String[] parts = template.split(TEMPLATES_KV_SEPARATOR);
                    return Pair.of(parts[0], parts[1]);
                })
                .filter(pair -> Bank.fromStr(pair.getLeft()) != null)
                .collect(Collectors.toMap(pair -> Bank.fromStr(pair.getLeft()), Pair::getRight));
    }
}
