package org.bot.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.bot.client.ScrapperClient
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StartCommandHandler(bot: Bot, private val scrapperClient: ScrapperClient): MessageHandler(bot) {

    private val log = LoggerFactory.getLogger(Bot::class.java)

    override fun handleMessage(update: Update) {
        val message: Message = update.message()
        val text = message.text().trim()
        val parts = text.split(" ".toRegex()).toTypedArray()

        val command = parts[0]

        if ("/start" == command) {
            if (parts.size == 1) {
                bot.send(SendMessageAdapter(message.chat().id(),
                        "Чтобы зарегистрировать пользователя, отправьте команду /start с id чата пользователя, "
                                +
                                "разделёнными пробелами."
                ).getSendMessage())
            } else {
                try {
                    val chatId = parts[1].toLong()
                    scrapperClient.registerChat(chatId)
                    bot.send(SendMessageAdapter(message.chat().id(), "Зарегистрирован чат с ID: $chatId")
                            .getSendMessage())
                } catch (e: NumberFormatException) {
                    log.error("Некорректный ID чата: {}", parts[1], e)
                    bot.send(SendMessageAdapter(message.chat().id(),
                            "ID чата должен быть числом: " + parts[1])
                            .getSendMessage())
                } catch (e: Exception) {
                    log.error("Ошибка регистрации чата: {}", e.message, e)
                    bot.send(SendMessageAdapter(message.chat().id(),
                            "Не удалось зарегистрировать чат: " + parts[1])
                            .getSendMessage())
                }
            }
        } else {
            nextHandler!!.handleMessage(update);
        }
    }
}