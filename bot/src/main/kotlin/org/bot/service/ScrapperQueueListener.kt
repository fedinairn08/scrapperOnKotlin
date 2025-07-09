package org.bot.service

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

    @RabbitHandler
    fun receiver(update: LinkUpdateRequest) {
        bot.send(SendMessageAdapter(update.tgChatIds.first(), update.description).getSendMessage())
    }
}