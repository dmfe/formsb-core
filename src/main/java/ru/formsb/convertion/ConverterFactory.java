package ru.formsb.convertion;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.formsb.dtos.request.DocType;

@Component
public class ConverterFactory {

    private final Map<DocType, Converter> convertersMap;

    public ConverterFactory(Set<Converter> converters) {
        convertersMap = converters.stream().collect(Collectors.toMap(Converter::getExportType, Function.identity()));
    }

    public Converter getConverter(DocType type) {
        return convertersMap.get(type);
    }
}
