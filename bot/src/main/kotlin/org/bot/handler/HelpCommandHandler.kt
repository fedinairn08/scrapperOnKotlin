package org.bot.handler

import com.pengrad.telegrambot.model.Update
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.springframework.stereotype.Component

@Component
class HelpCommandHandler(bot: Bot): MessageHandler(bot) {

    private var helpMessage: String? = null

    override fun handleMessage(update: Update) {
        val message = update.message()
        if (message.text() == "/help") {
            bot.send(SendMessageAdapter(message.chat().id(), getHelpMessage()).getSendMessage())
        } else {
            nextHandler!!.handleMessage(update)
        }

    }

    private fun getHelpMessage(): String {
        if (helpMessage == null) {
            helpMessage = """
                    /start -- зарегистрировать пользователя
                    /help -- вывести окно с командами
                    /track -- начать отслеживание ссылки
                    /untrack -- прекратить отслеживание ссылки
                    /list -- показать список отслеживаемых ссылок
                    """.trimIndent()
        }
        return helpMessage!!
    }
}