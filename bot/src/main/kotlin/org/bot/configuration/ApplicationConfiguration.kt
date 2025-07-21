package org.bot.configuration

import com.pengrad.telegrambot.TelegramBot
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
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

    @Bean
    fun rabbitCounter(prometheusMeterRegistry: MeterRegistry?): Counter {
        return Counter.builder("rabbitmq.messages.processed")
            .description("Number of processed RabbitMQ messages")
            .register(prometheusMeterRegistry!!)
    }
}
