package org.scrapper.configuration

import lombok.RequiredArgsConstructor
import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@RequiredArgsConstructor
class RabbitMQConfiguration(private val rabbitMQConfig: RabbitMQConfig) {
    @Bean
    fun directExchange(): DirectExchange {
        return DirectExchange(rabbitMQConfig.exchange)
    }

    @Bean
    fun queue(): Queue {
        return QueueBuilder.durable(rabbitMQConfig.queue)
            .withArgument("x-dead-letter-exchange", rabbitMQConfig.queue + ".dlx")
            .build()
    }

    @Bean
    fun binding(directExchange: DirectExchange?, queue: Queue?): Binding {
        return BindingBuilder.bind(queue).to(directExchange).with(rabbitMQConfig.queue)
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }
}