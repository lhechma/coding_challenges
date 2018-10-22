package com.n26.web.configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonCustomizer {

    @Bean
    Module timeModule() {
        return new JavaTimeModule();
    }

}
