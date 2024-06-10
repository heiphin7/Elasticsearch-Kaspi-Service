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
        // Обработка callback queries от inline кнопок
        if (update.hasCallbackQuery()) {
            handleCallbackQuery(update.getCallbackQuery(), update.getMessage().getText());
        }

        // Обработка обычных текстовых сообщений
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            // Создаем объект SendMessage для отправки сообщения с выбором опций
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("Выберите опцию:");
            message.setReplyMarkup(getInlineKeyboard());

            // Отправляем сообщение с inline клавиатурой
            execute(message);
        }
    }

    // Метод для обработки callback query
    private void handleCallbackQuery(CallbackQuery callbackQuery, String query) throws TelegramApiException {
        String callData = callbackQuery.getData();
        Long chatId = callbackQuery.getMessage().getChatId();

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        if ("button1".equals(callData)) {
            List<Product> products
                    = kaspiParser.parseByQuery(query);

            StringBuilder result = new StringBuilder();

            for(Product product: products) {
                result.append("\n").append(product.toString());
            }

            if (result.isEmpty()) {
                result = new StringBuilder("Ничего не найдено!");
            }

            message.setText(result.toString());
        } else if ("button2".equals(callData)) {
            message.setText("Вы выбрали 'Парсинг по коду товара'.");
            // Здесь можно добавить дополнительные действия по парсингу
        } else {
            message.setText("Что-то пошло не так, попробуйте снова.");
        }

        execute(message);
    }

    public static InlineKeyboardMarkup getInlineKeyboard() {
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

    @Override
    public String getBotUsername() {
        return "My-first-bot";
    }

    public TelegramBot(DefaultBotOptions options, String botToken) {
        super(options, botToken);
    }
}
