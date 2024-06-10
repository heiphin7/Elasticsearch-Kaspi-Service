package com.parser.parser.openAI;

import lombok.Data;

@Data
public class Message {
    private String role;
    private String content;
}
