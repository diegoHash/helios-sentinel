package com.helios.platform.sentinel.configuration;

import com.helios.platform.sentinel.error.TelegramNotificationError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public TelegramNotificationError telegramNotificationError(
            @Value("${application.telegram.bot.token:}") String botToken,
            @Value("${application.telegram.bot.chat-id:}") String chatId,
            @Value("${application.telegram.bot.username:cac_access_control_bot}") String botUsername) {
        return new TelegramNotificationError(botToken, chatId, botUsername);
    }
}
