package com.parser.parser.telegrambot;

import com.parser.parser.models.Product;
import com.parser.parser.parsers.KaspiParser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private KaspiParser kaspiParser;

    @SneakyThrows // for exception in sendApiMethod
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            List<Product> products
                    = kaspiParser.parseByQuery(update.getMessage().getText());

            StringBuilder result = new StringBuilder();

            for(Product product: products) {
                result.append("\n").append(product.toString());
            }

            if (result.isEmpty()) {
                result = new StringBuilder("Ничего не найдено!");
            }

            var chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage(chatId.toString(), result.toString());
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
