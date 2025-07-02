package org.bot.configuration

import com.pengrad.telegrambot.TelegramBot
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RequiredArgsConstructor
class ApplicationConfiguration(private val config: ApplicationConfig) {

    @Bean
    fun telegramBot():TelegramBot {
        return TelegramBot(config.token)
    }
}