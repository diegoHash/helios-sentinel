package com.helios.platform.sentinel.error;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TelegramNotificationError extends TelegramLongPollingBot {
    private static final Logger LOGGER = Logger.getLogger(TelegramNotificationError.class.getName());
    private final String botToken;
    private final String chatId;
    private final String botUsername;


    public TelegramNotificationError(String botToken, String chatId, String botUsername) {
        this.botToken = botToken == null ? "" : botToken.trim();
        this.chatId = chatId == null ? "" : chatId.trim();
        this.botUsername = botUsername == null ? "" : botUsername.trim();
    }

    private boolean isConfigured() {
        return !botToken.isBlank() && !chatId.isBlank();
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }


    @Override
    public String getBotToken() {
        return botToken;

    }

    @Override
    public void onUpdateReceived(Update update) {
      /* if (update.hasMessage() && update.getMessage().hasText()){
           String mssge = update.getMessage().getText();
           if (mssge.equals("/reportehoy")){
               sendMessage("Indicar fecha en formato YYYY-MM-DD");
           } else if (mssge.equals("/start")) {
               sendMessage("Bienvenido a CAC");
           }
           else{
               String enddate = mssge + " 23:59";
               String initialDate = mssge;
               String startDate = mssge + " 00:00";
           }
       }*/
    }

    public void notifyError(String msg) {

        if (!isConfigured()) {
            LOGGER.fine("Telegram notification skipped because the integration is not configured");
            return;
        }

        SendMessage message = new SendMessage();
        message.setText(msg);
        message.setChatId(chatId);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            LogFile.writeLogError(e, false);
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e, () -> "");
        }

    }

    public void sendReportAsFile(String reportContent) {
        if (!isConfigured()) {
            LOGGER.fine("Telegram report skipped because the integration is not configured");
            return;
        }
        // Crear un archivo temporal
        File tempFile = null;
        try {
            tempFile = File.createTempFile("report", ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                writer.write(reportContent);
            }

            // Enviar el archivo a Telegram
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);
            sendDocument.setDocument(new InputFile(tempFile));

            execute(sendDocument);
        } catch (IOException | TelegramApiException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e, () -> "");
        } finally {
            // Eliminar el archivo temporal después de enviarlo
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public void sendMessage(String msg) {
        if (!isConfigured()) {
            LOGGER.fine("Telegram message skipped because the integration is not configured");
            return;
        }
        SendMessage message = new SendMessage();
        message.setText(msg);
        message.setChatId(chatId);
        try {

            execute(message);
        } catch (TelegramApiException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e, () -> "");
        }
    }
}
