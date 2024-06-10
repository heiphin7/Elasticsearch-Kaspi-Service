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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private KaspiParser kaspiParser;

//    @SneakyThrows
//    @Override
//    public void onUpdateReceived(Update update) {
//        if (update.hasCallbackQuery()) {
//            String callData = update.getCallbackQuery().getData();
//            Long chatId = update.getCallbackQuery().getMessage().getChatId();
//
//            if ("button1".equals(callData)) {
//                sendApiMethod(new SendMessage(chatId.toString(), "Вы нажали на кнопку 1"));
//            } else if ("button2".equals(callData)) {
//                sendApiMethod(new SendMessage(chatId.toString(), "Вы нажали на кнопку 2"));
//            }
//        }
//    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            // Создаем объект SendMessage для отправки сообщения
            SendMessage message = new SendMessage();
            message.setChatId(chatId.toString());
            message.setText("Выберите опцию:");

            // Привязываем inline клавиатуру к сообщению
            message.setReplyMarkup(getInlineKeyboard());

            // todo actions after select buttons
            if (update.getCallbackQuery().equals("Парсинг по запросу")) {
                sendApiMethod(new SendMessage(chatId.toString(), "ты выбрал первый:)"));

                // Создаем объект SendMessage для отправки сообщения
                SendMessage messageAfterSelect = new SendMessage();
                messageAfterSelect.setChatId(chatId.toString());
                messageAfterSelect.setText("Выберите опцию:");

                // Привязываем inline клавиатуру к сообщению
                messageAfterSelect.setReplyMarkup(getInlineKeyboard());

                // Отправляем сообщение
                execute(messageAfterSelect);
            } else if (update.getCallbackQuery().equals("Парсинг по коду товара")) {
                sendApiMethod(new SendMessage(chatId.toString(), "ты выбрал второй вариант"));

                // Создаем объект SendMessage для отправки сообщения
                SendMessage messageAfterSelect = new SendMessage();
                messageAfterSelect.setChatId(chatId.toString());
                messageAfterSelect.setText("Выберите опцию:");

                // Привязываем inline клавиатуру к сообщению
                messageAfterSelect.setReplyMarkup(getInlineKeyboard());

                // Отправляем сообщение
                execute(messageAfterSelect);
            } else {
                sendApiMethod(new SendMessage(chatId.toString(), "Что-то пошло не так"));

                // Создаем объект SendMessage для отправки сообщения
                SendMessage messageAfterSelect = new SendMessage();
                messageAfterSelect.setChatId(chatId.toString());
                messageAfterSelect.setText("Выберите опцию:");

                // Привязываем inline клавиатуру к сообщению
                messageAfterSelect.setReplyMarkup(getInlineKeyboard());

                // Отправляем сообщение
                execute(messageAfterSelect);
            }
        }
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

