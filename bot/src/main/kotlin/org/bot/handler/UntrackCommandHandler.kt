package org.bot.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.springframework.stereotype.Component

@Component
class UntrackCommandHandler(bot: Bot): MessageHandler(bot) {
    override fun handleMessage(update: Update) {
        val message: Message = update.message()
        if (message.text().equals("/untrack")) {
            bot.send(SendMessageAdapter(message.chat().id(), "untrack").getSendMessage());
        } else {
            nextHandler!!.handleMessage(update);
        }
    }
}