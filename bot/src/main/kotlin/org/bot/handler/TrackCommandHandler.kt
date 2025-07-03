package org.bot.handler

import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.bot.client.ScrapperClient
import org.bot.dto.scrapper.request.AddLinkRequest
import org.bot.tg.Bot
import org.bot.tg.SendMessageAdapter
import org.linkparser.service.LinkParser
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component
import java.net.URI

@Component
@ComponentScan("org.linkparser.service")
class TrackCommandHandler(
        bot: Bot,
        private val scrapperClient: ScrapperClient,
        private val linkParser: LinkParser
): MessageHandler(bot) {

    private val log = LoggerFactory.getLogger(Bot::class.java)

    override fun handleMessage(update: Update) {
        val message: Message = update.message()
        val chatId = message.chat().id()
        val stringUri = message.text().trim().split(" ")
        val commandMessage = stringUri[0]
        var successCount = 0

        if ("/track" == commandMessage) {
            if (stringUri.size == 1) {
                val messageForGetLink = ("Чтобы добавить ссылку отправьте команду "
                        + "/track с нужными ссылками, разделенными пробелами.")
                bot.send(SendMessageAdapter(chatId, messageForGetLink)
                        .getSendMessage())
            } else {
                val urls = parseUris(stringUri)
                val sb = StringBuilder()

                if (urls.isNotEmpty()) {
                    sb.append("Добавлены следующие ссылки:\n")
                    for (uri in urls) {
                        val response = scrapperClient.addLink(chatId, AddLinkRequest(uri))
                        response.ifPresent { linkResponse -> sb.append(linkResponse.url.toString()) }
                        successCount++
                    }
                }

                val summary = String.format(
                        "Добавлено %d из %d ссылок\n",
                        successCount,
                        urls.size
                )

                sb.insert(0, summary)

                bot.send(SendMessageAdapter(chatId, sb.toString())
                        .getSendMessage())
            }
        } else {
            nextHandler!!.handleMessage(update);
        }
    }

    private fun parseUris(stringUris: List<String>): List<URI> {
        val uris = mutableListOf<URI>()
        for (s in stringUris) {
            try {
                val uri = URI(s)
                if (linkParser.parseUrl(uri) != null) {
                    uris.add(uri)
                }
            } catch (e: Exception) {
                log.error(e.message)
            }
        }
        return uris
    }
}