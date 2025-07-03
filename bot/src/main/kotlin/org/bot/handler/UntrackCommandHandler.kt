package org.bot.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.bot.client.ScrapperClient
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URISyntaxException

@Component
class UntrackCommandHandler(bot: Bot, private val scrapperClient: ScrapperClient): MessageHandler(bot) {

    private val log = LoggerFactory.getLogger(Bot::class.java)

    override fun handleMessage(update: Update) {
        val message: Message = update.message()
        val chatId = message.chat().id()
        val text = message.text().trim()

        val parts = text.split(" ").toMutableList()
        val command = parts.removeFirst()
        var successCount = 0

        if ("/untrack" == command) {
            if (parts.isEmpty()) {
                val helpMessage = ("Чтобы удалить ссылку, отправьте команду /untrack с нужными ссылками, "
                        +
                        "разделёнными пробелами.")
                bot.send(SendMessageAdapter(chatId, helpMessage).getSendMessage())
                return
            }

            val uris = parseUris(parts)
            val responseBuilder = StringBuilder()

            if (uris.isEmpty()) {
                responseBuilder.append("Ни одну из указанных ссылок не удалось распознать.")
            } else {
                for (rawUri in parts) {
                    try {
                        val uri = URI(rawUri)
                        val result = scrapperClient.removeLink(chatId, uri)
                        if (result) {
                            successCount++
                            responseBuilder.append("✅ ").append(uri).append("\n")
                        } else {
                            responseBuilder.append("❌ Не найдена ссылка: ").append(uri).append("\n")
                        }
                    } catch (e: Exception) {
                        responseBuilder.append("⚠️ Некорректная ссылка: ").append(rawUri).append("\n")
                    }
                }
            }

            val summary = String.format(
                    "Удалено %d из %d ссылок\n",
                    successCount,
                    parts.size
            )

            responseBuilder.insert(0, summary)

            bot.send(SendMessageAdapter(chatId, responseBuilder.toString()).getSendMessage())
        } else {
            nextHandler!!.handleMessage(update);
        }
    }

    private fun parseUris(strings: List<String>): List<URI> {
        val result = mutableListOf<URI>()
        for (s in strings) {
            try {
                result.add(URI(s))
            } catch (e: URISyntaxException) {
                log.warn("Невалидный URI: {}", s)
            }
        }
        return result
    }
}