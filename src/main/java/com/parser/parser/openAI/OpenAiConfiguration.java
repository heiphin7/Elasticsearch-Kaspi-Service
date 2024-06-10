package com.parser.parser.openAI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAiConfiguration {

    @Bean
    public OpenAI OpenAI(@Value("${openai.token}") String token, RestTemplate restTemplate) {
        return new OpenAI(token, restTemplate);
    }

}
