package com.parser.parser.telegrambot;

import com.parser.parser.models.Product;
import com.parser.parser.parsers.KaspiParser;
import com.parser.parser.repository.SearchRepository;
import com.parser.parser.service.ProductService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private KaspiParser kaspiParser;

    private Map<Long, String> userStates = new HashMap<>();

    @Autowired
    private ProductService productService;

    @Autowired
    private SearchRepository searchRepository;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        var chatId = update.getMessage().getChatId();
        var message = update.getMessage().getText();

        long time = System.currentTimeMillis();

        String response = "";

        List<Product> products = searchRepository.findByTitle(message);

        int size = products.size() < 10 ? products.size(): 11;

        for (int i = 0; i < size; i++) {
            response += products.get(i) + "\n";
        }

        long timesInMillis = System.currentTimeMillis() - time;

        if(response == null || response.isEmpty()) {
            response = "Ничего не найдено, попробуйте заново";
        } else {
            response += "Время поиска: " + timesInMillis + " ms";
        }

        SendMessage message1 = new SendMessage(chatId.toString(), response);
        sendApiMethod(message1);
    }

    @Override
    public String getBotUsername() {
        return "My-first-bot";
    }

    public TelegramBot(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }
}
