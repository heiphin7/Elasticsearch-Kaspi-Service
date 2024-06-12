package com.parser.parser.openAI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfiguration {

    @Bean
    public OpenAI OpenAI(@Value("${api-key.token}") String token, RestTemplateBuilder restTemplate) {
        return new OpenAI(token, restTemplate.build(), "openai-community/gpt2");
    }

}
