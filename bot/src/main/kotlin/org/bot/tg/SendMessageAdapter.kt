package org.bot.tg

import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage

class SendMessageAdapter(chatId: Long, text: String) {

    private val message: SendMessage = SendMessage(chatId, text)
            .parseMode(ParseMode.Markdown)

    fun getSendMessage(): SendMessage {
        return message
    }
}