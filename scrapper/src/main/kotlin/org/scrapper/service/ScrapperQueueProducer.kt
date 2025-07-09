package org.scrapper.service

import lombok.RequiredArgsConstructor
import org.scrapper.configuration.RabbitMQConfig
import org.scrapper.dto.request.LinkUpdateRequest
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class ScrapperQueueProducer(private val rabbitTemplate: RabbitTemplate, private val rabbitMQConfig: RabbitMQConfig) {

    fun send(update: LinkUpdateRequest) {
        rabbitTemplate.convertAndSend(
            rabbitMQConfig.exchange,
            rabbitMQConfig.queue,
            update
        )
    }
}