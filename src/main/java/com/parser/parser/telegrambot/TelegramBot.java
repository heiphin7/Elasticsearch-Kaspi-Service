package com.parser.parser.telegrambot;

import com.parser.parser.models.Product;
import com.parser.parser.parsers.KaspiParser;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private KaspiParser kaspiParser;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) { // handle callBack
            handleCallBackQuery(update.getCallbackQuery());
        } else { // handle user's message
            handleUserMessage(update);
        }
    }

    // hander for buttons
    @SneakyThrows
    private void handleCallBackQuery(CallbackQuery callbackQuery) {
        String callData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        if (callData.equals("button1")) {
            userStates.put(chatId, "awaiting_query");
            sendApiMethod(new SendMessage(chatId.toString(), "Введите ваш запрос для парсинга:"));
        } else if (callData.equals("button2")) {
            userStates.put(chatId, "awaiting_product_id");
            sendApiMethod(new SendMessage(chatId.toString(), "Введите ID продукта для парсинга:"));
        }
    }

    // handler for user's message
    @SneakyThrows
    private void handleUserMessage(Update update) {
        String messageText = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        SendMessage message = new SendMessage();

        if (messageText.equals("/start")) {
            // send hello message
            message.setChatId(chatId.toString());
            message.setText("Привет, это бот который \"парсит\" твои запросы по магазину Kaspi. Задача бота - быстро и легко давать информацию о товаре. \n \n" +
                        "Типы парсинга: \n" +
                        "1. Парсинг по коду товара, это когда вы должны ввести код товара в магазине Kaspi, затем вам будет дана информация об этом товаре \n" +
                        "2. Парсинг по запросу, это когда вы должны ввести какой-либо запрос, например: \"телефоны\", а далее бот ищет результаты и пришлёт их вам \n \n" +
                        "Чтобы начать, выберите опцию:");
           message.setReplyMarkup(getInlineKeyboard());

           // todo handle /clear method
        } else {
            message.setChatId(chatId.toString());
            message.setText(
                    "Чтобы начать и получить инструкции, введите /start. \n \n" +
                    "Список доступных команд: \n" +
                    "/start - начать диалог" +
                    "/clear - очистить чат"
            );
        }

        sendApiMethod(message);
    }

    @Override
    public String getBotUsername() {
        return "My-first-bot";
    }

    public TelegramBot(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }

    // inline buttons
    public InlineKeyboardMarkup getInlineKeyboard() {
        // Создаем объект InlineKeyboardMarkup
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();

        // Список строк для клавиатуры
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Список кнопок для одной строки
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        // Создаем кнопку и устанавливаем текст и callback данные
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Парсинг по запросу");
        button1.setCallbackData("button1");
        rowInline.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Парсинг по коду товара");
        button2.setCallbackData("button2");
        rowInline.add(button2);

        // Добавляем строку кнопок в список строк
        rowsInline.add(rowInline);

        // Устанавливаем список строк в объект InlineKeyboardMarkup
        markupInline.setKeyboard(rowsInline);

        // Возвращаем сконфигурированный InlineKeyboardMarkup
        return markupInline;
    }
}
