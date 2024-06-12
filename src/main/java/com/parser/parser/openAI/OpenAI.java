package com.parser.parser.openAI;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class OpenAI {

    private String token;
    private final RestTemplate restTemplate;
    private String modelId;

    public ChatCompletionObject chatCompletionObject(String message) {
        String url = "https://api-inference.huggingface.co/models/" + modelId; // API from Huggingface
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-type", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> http = new HttpEntity<>(message, headers);
        System.out.println(http);
        ResponseEntity<ChatCompletionObject> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, http, ChatCompletionObject.class
        );

        return responseEntity.getBody();
    }
}

        /*
         for ChatGPT
        String url = "https://api.openai.com/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-type", "application/json");

        String request = """
                {
                    "model": "text-embedding-3-small",
                    "prompt": "%s",
                    "max_tokens": 7,
                    "temperature": 0
                  }
                """.formatted(message);

        HttpEntity<String> http = new HttpEntity<>(request, headers);
        System.out.println(http);
        ResponseEntity<ChatCompletionObject> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST ,http, ChatCompletionObject.class
        );

        return responseEntity.getBody();
         */
