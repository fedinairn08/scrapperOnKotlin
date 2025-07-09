package org.bot.configuration

import lombok.RequiredArgsConstructor
import org.scrapper.dto.request.LinkUpdateRequest
import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.ClassMapper
import org.springframework.amqp.support.converter.DefaultClassMapper
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
    fun deadLetterExchange(): FanoutExchange {
        return FanoutExchange(rabbitMQConfig.queue + ".dlx")
    }

    @Bean
    fun queue(): Queue {
        return QueueBuilder.durable(rabbitMQConfig.queue)
            .withArgument("x-dead-letter-exchange", rabbitMQConfig.queue + ".dlx")
            .build()
    }

    @Bean
    fun deadLetterQueue(): Queue {
        return QueueBuilder.durable(rabbitMQConfig.queue + ".dlq").build()
    }

    @Bean
    fun binding(directExchange: DirectExchange?, queue: Queue?): Binding {
        return BindingBuilder.bind(queue).to(directExchange).with(rabbitMQConfig.queue)
    }

    @Bean
    fun deadLetterBinding(deadLetterQueue: Queue?, deadLetterExchange: FanoutExchange?): Binding {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange)
    }

    @Bean
    fun classMapper(): ClassMapper {
        val mappings: MutableMap<String, Class<*>> = HashMap()
        mappings["scrapper.scrapper.dto.request.LinkUpdateRequest"] = LinkUpdateRequest::class.java

        val classMapper = DefaultClassMapper()
        classMapper.setTrustedPackages("scrapper.scrapper.dto.request.*")
        classMapper.setIdClassMapping(mappings)
        return classMapper
    }

    @Bean
    fun jsonMessageConverter(classMapper: ClassMapper?): MessageConverter {
        val jsonConverter = Jackson2JsonMessageConverter()
        jsonConverter.setClassMapper(classMapper!!)
        return jsonConverter
    }
}