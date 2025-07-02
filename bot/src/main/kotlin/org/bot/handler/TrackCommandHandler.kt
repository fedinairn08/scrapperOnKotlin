package org.bot.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.springframework.stereotype.Component

@Component
class TrackCommandHandler(bot: Bot): MessageHandler(bot) {
    override fun handleMessage(update: Update) {
        val message: Message = update.message()
        if (message.text().equals("/track")) {
            bot.send(SendMessageAdapter(message.chat().id(), "track").getSendMessage());
        } else {
            nextHandler!!.handleMessage(update);
        }
    }
}