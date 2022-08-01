package org.voroshilkin;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public class NoteBot extends TelegramLongPollingBot {

    private final Translator translator = new Translator();

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            try {

                String text = update.getMessage().getText();

                String result = null;
                try {
                    result = translator.translateToRussian(text);
                } catch (IOException e) {

                    SendMessage errorMessage = createSendMessage(update, "Извините, что-то пошло не так во время перевода :(");

                    execute(errorMessage);
                    return;
                }

                SendMessage message = createSendMessage(update, result);

                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage createSendMessage(Update update, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(update.getMessage().getChatId());
        return message;
    }

    @Override
    public String getBotUsername() {
        return "TestingNote_bot";
    }

    @Override
    public String getBotToken() {
        return "5474242708:AAG96d0MGnpdU1kOKsAS1JL5YirUe5tF4UM";
    }
}