package com.helios.platform.sentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootApplication
public class HeliosSentinelApplication {

    public static void main(String[] args) throws TelegramApiException {
        SpringApplication.run(HeliosSentinelApplication.class, args);
    }

}
