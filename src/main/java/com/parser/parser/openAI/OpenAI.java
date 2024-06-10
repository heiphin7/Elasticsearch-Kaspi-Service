package com.parser.parser.openAI;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class OpenAI {

    private final String token;
    private final RestTemplate restTemplate;

    public ChatCompletionObject chatCompletionObject(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-type", "application/json");

        String request = """
                {
                    "model": "gpt-3.5-turbo-instruct",
                    "prompt": "%s"
                    "max_tokens": 7,
                    "temperature": 0
                  }
                """.formatted(message);

        HttpEntity<String> http = new HttpEntity<>(request, headers);
        ResponseEntity<ChatCompletionObject> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST ,http, ChatCompletionObject.class
        );

        return responseEntity.getBody();
    }
}
