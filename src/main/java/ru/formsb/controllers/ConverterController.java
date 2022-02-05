package ru.formsb.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConverterController {

    @GetMapping("/test")
    public String test() {
        return "{\"message\": \"Hello, world!\"}";
    }
}
