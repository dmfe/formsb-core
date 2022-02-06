package ru.formsb.Configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BaseConfig {

    @Value("${base.uri}")
    private String baseUri;
}
