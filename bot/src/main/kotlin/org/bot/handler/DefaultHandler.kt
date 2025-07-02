package org.bot.handler

import com.pengrad.telegrambot.model.Update
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.springframework.stereotype.Component

@Component
class DefaultHandler(bot: Bot): MessageHandler(bot) {
    override fun handleMessage(update: Update) {
        bot.send(
                SendMessageAdapter(update.message().chat().id(), "Нет подходящего обработчика").getSendMessage()
        )
    }
}