package com.parser.parser;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {

    @SneakyThrows // for exception in sendApiMethod
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().toString());
            var text = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage(chatId.toString(), text);
            sendApiMethod(message);
        }
    }

    @Override
    public String getBotUsername() {
        return "My-first-bot";
    }

    // Constructure for initialize bot
    public TelegramBot(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }
}
