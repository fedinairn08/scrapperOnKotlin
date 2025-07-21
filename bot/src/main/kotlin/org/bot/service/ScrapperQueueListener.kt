package org.bot.service

import io.micrometer.core.instrument.Counter
import lombok.RequiredArgsConstructor
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.scrapper.dto.request.LinkUpdateRequest
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
@RabbitListener(queues = ["\${rabbit.queue}"])
class ScrapperQueueListener(private val bot: Bot) {

    private val rabbitCounter: Counter? = null

    @RabbitHandler
    fun receiver(update: LinkUpdateRequest) {
        bot.send(SendMessageAdapter(update.tgChatIds.first(), update.description).getSendMessage())
        rabbitCounter?.increment()
    }
}
