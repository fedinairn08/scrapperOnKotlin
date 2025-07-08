package org.scrapper.service

import org.scrapper.entity.Chat
import java.util.*

interface ChatService {

    fun register(chatId: Long)

    fun unregister(chatId: Long)

    fun getByChatId(tgChatId: Long): Optional<Chat>
}