package org.bot.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.bot.client.ScrapperClient
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.springframework.stereotype.Component

@Component
class ListCommandHandler(bot: Bot, private val scrapperClient: ScrapperClient): MessageHandler(bot) {
    override fun handleMessage(update: Update) {
        val message: Message = update.message()
        if (message.text().equals("/list")) {
            val response = scrapperClient.getLinks(message.chat().id())

            if (response.isPresent) {
                val listLinksResponse= response.get()

                if (listLinksResponse.links.isNotEmpty()) {
                    val sb = StringBuilder()
                    sb.append("Сейчас отслеживаются ссылки:\n")
                    for (linksResponse in response.get().links) {
                        sb.append(linksResponse.url.toString()).append("\n")
                    }
                    bot.send(SendMessageAdapter(message.chat().id(), sb.toString()).getSendMessage())
                } else {
                    val answer = "Сейчас не отслеживаются ссылки"
                    bot.send(SendMessageAdapter(message.chat().id(), answer).getSendMessage())
                }
            } else {
                bot.send(SendMessageAdapter(message.chat().id(), "Непредвиденная ошибка. Повторите позже")
                        .getSendMessage())
            }
        } else {
            nextHandler!!.handleMessage(update);
        }
    }
}