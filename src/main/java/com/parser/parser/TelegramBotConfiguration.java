package com.parser.parser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class TelegramBotConfiguration {

    @Bean
    public TelegramBot telegramBot(@Value("${bot.token}") String botToken) {
        var botOptions = new DefaultBotOptions();
        return new TelegramBot(botOptions, botToken);
    }

}

